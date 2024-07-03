import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

data class RecordingViewModel(
    val fileName: String,
    val lastModified: Date,
    val size: Long,
)

class RecordingsViewModel(app: Application): AndroidViewModel(app) {
    sealed class State {
        data object Loading: State()
        data class Data(val data: List<RecordingViewModel>): State()
    }

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load(setLoading: Boolean = true) {
        if (setLoading) {
            _state.value = State.Loading
        }
        viewModelScope.launch {
            if (isActive) {
                val data = makeDataLoadCallOnEntry()
                _state.value = State.Data(data)
            }
        }
    }

    fun remove(recordingName: String): Boolean {
        val context = getApplication<Application>()
        val recordingDirectory = File(context.filesDir, recordingName)
        val successful = recordingDirectory.deleteRecursively()
        val recordingZipArchive = File(context.filesDir, "${recordingName}.zip")
        recordingZipArchive.delete()
        load(setLoading = false)
        return successful
    }

    private fun makeDataLoadCallOnEntry(): List<RecordingViewModel> {
        val context = getApplication<Application>()
        val recordingNameRegex = """^recording_[a-z0-9\-]+(?!\.zip)$""".toRegex()
        return context.filesDir.list { f: File, s: String ->
            // recording_<UUID>
            f.isDirectory && (recordingNameRegex.matches(s) || s.endsWith(".zip"))
        }?.map { fileName ->
            val f = File(context.filesDir, fileName)
            val fileSize = f.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
            val size = ((fileSize/1024)/1024)
            RecordingViewModel(fileName, Date(f.lastModified()), size)
        }?.sortedByDescending { it.lastModified }?.toList() ?: listOf()
    }
}

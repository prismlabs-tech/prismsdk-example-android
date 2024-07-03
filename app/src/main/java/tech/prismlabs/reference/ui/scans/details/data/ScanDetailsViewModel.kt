package tech.prismlabs.reference.ui.scans.details.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.prismlabs.prismsdk.api_client.AssetUrls
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.prismsdk.api_client.ScanClient
import tech.prismlabs.prismsdk.api_client.ScanStatus
import tech.prismlabs.reference.scans.Downloader
import tech.prismlabs.reference.scans.PrismClient
import tech.prismlabs.reference.settings.SettingsService
import java.io.File

data class ScanResult(val scan: Scan)

class ScanDetailsViewModel(app: Application) : AndroidViewModel(app) {
    sealed class State {
        data object Loading: State()
        data class Data(val scan: Scan): State()
    }

    private val context = getApplication<Application>()
    private val settingsService = SettingsService(context)
    private val scanClient = ScanClient(PrismClient.getSharedInstance())

    private var _avatar = MutableStateFlow<File?>(null)
    val avatar = _avatar.asStateFlow()

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val scan: Scan?
        get() = _state.value.let {
            if (it is State.Data) {
                it.scan
            } else {
                null
            }
        }

    fun fetchDetails(id: String) {
        Log.i("ScanDetailsViewModel", "fetchDetails for scan: $id")
        //TODO: Handle the downloading and fetching of the assets needed to render the view
        viewModelScope.launch {
            val scan = scanClient.getScan(id).fold(
                onFailure = {
                    Log.e("ScanDetailsViewModel", "Error: $it")
                    return@fold null
                },
                onSuccess = { response ->
                    Log.i("ScanDetailsViewModel", "Fetched Scan: ${response.id}")
                    return@fold response
                }
            )
            if (scan == null) {
                Log.e("ScanDetailsViewModel", "Scan is null")
                return@launch
            }

            when (scan.status) {
                ScanStatus.CREATED -> {
                    Log.i("ScanDetailsViewModel", "Scan is created and pending upload/processing")
                }
                ScanStatus.PROCESSING -> {
                    Log.i("ScanDetailsViewModel", "Scan is processing")
                }
                ScanStatus.FAILED -> {
                    Log.i("ScanDetailsViewModel", "Scan failed")
                }
                ScanStatus.READY -> {
                    Log.i("ScanDetailsViewModel", "Scan is ready")
                    val assets = scanClient.assetUrls(id).fold(
                        onFailure = {
                            Log.e("ScanDetailsViewModel", "Error: $it")
                            return@fold null
                        },
                        onSuccess = { response ->
                            Log.i("ScanDetailsViewModel", "Fetched Assets")
                            return@fold response
                        }
                    )

                    if (assets != null) {
                        downloadAssets(assets, id)
                    }
                }
            }
            _state.update {
                State.Data(scan)
            }
        }
    }

    private fun downloadAssets(assetUrls: AssetUrls, id: String) {
        val scanDirectory = File(context.filesDir, "scan_$id")

        // If the directory doesn't exist, create it
        if (!scanDirectory.exists()) {
            scanDirectory.mkdirs()
        } else {
            // If the directory exists, check if the model exists
            val extension = "ply" // For now we have this hardcoded, but we should get it from the assetUrls
            val avatar = File(scanDirectory, "avatar.$extension")
            if (avatar.exists()) {
                _avatar.update { avatar }
                return
            }
        }

        assetUrls.model?.let {
            downloadModel(it, scanDirectory)
        }
    }

    private fun downloadModel(url: String, scanDirectory: File) {
        val extension = "ply" // For now we have this hardcoded, but we should get it from the assetUrls
        val file = File(scanDirectory, "avatar.$extension")
        if (!file.exists()) {
            viewModelScope.launch {
                Downloader().download(url, file)
                _avatar.update { file }
            }
        }
    }

    suspend fun delete(scan: Scan) {
        scanClient.deleteScan(scan.id)
    }
}
package tech.prismlabs.reference.ui.scans.list

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.prismlabs.prismsdk.api_client.NewScan
import tech.prismlabs.prismsdk.api_client.PageInfo
import tech.prismlabs.prismsdk.api_client.Paginated
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.prismsdk.api_client.ScanClient
import tech.prismlabs.prismsdk.api_client.SortOrder
import tech.prismlabs.reference.scans.GroupedScan
import tech.prismlabs.reference.scans.PrismClient
import tech.prismlabs.reference.scans.RefreshTimer
import tech.prismlabs.reference.scans.date
import tech.prismlabs.reference.scans.grouped
import tech.prismlabs.reference.scans.list
import tech.prismlabs.reference.settings.SettingsService
import tech.prismlabs.reference.uploading.Uploader
import java.io.File

private const val PAGE_SIZE_LIMIT = 10

class ScanListViewModel(app: Application) : AndroidViewModel(app) {
    sealed class State {
        data object Loading: State()
        data class Data(val data: List<GroupedScan>): State()
    }

    enum class ListState {
        IDLE, LOADING, LOADED
    }

    private val context = getApplication<Application>()
    private val settingsService = SettingsService(context)
    private val scanClient = ScanClient(PrismClient.getSharedInstance())
    private val uploader = Uploader()
    val uploadProgress = uploader.uploadProgress

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val scans: List<GroupedScan>
        get() = _state.value.let {
            if (it is State.Data) {
                it.data
            } else {
                emptyList()
            }
        }

    private var pageInfo by mutableStateOf<PageInfo?>(null)
    private var hasScanned by mutableStateOf(false)
    private var lastScanId by mutableStateOf<String?>( null)

    var canPaginate by mutableStateOf(false)
    var listState by mutableStateOf(ListState.IDLE)

    private companion object {
        const val REFRESH_INTERVAL = 10000L // 10 seconds in milliseconds
    }

    private var refreshTimer = RefreshTimer.getSharedTimer()
    private var refreshJob: Job? = null

    init {
        Log.i("ScanListViewModel", "Initializing ScanListViewModel")
        fetch()

        viewModelScope.launch {
            uploader.uploadProgress.collect { progress ->
                Log.i("ScanListViewModel", "Upload Progress: $progress")
            }
        }

        // Start refreshing when the ViewModel is active
        viewModelScope.launch {
            while (true) {
                refreshScans()
                delay(REFRESH_INTERVAL)
            }
        }.also { refreshJob = it }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }

    fun fetch(loading: Boolean = false) {
        if (loading) {
            _state.update { State.Loading }
        }
        viewModelScope.launch {
            fetchMoreScans()
        }
    }

    fun remove(scan: Scan): Boolean {
        val currentScans = scans.list()
        val newScans = currentScans.toMutableList()
        newScans.remove(scan)
        _state.value = State.Data(newScans.grouped())

        viewModelScope.launch {
            scanClient.deleteScan(scan.id)
        }
        return true
    }

    fun remove(id: String): Boolean {
        val scan = scans.find { groupedScans -> groupedScans.scans.any { it.id == id } }?.scans?.find { it.id == id }
        if (scan != null) {
            return remove(scan)
        }
        return false
    }

    private suspend fun fetchMoreScans() {
        listState = ListState.LOADING
        val cursor = pageInfo?.cursor
        Log.i("ScanListViewModel", "Fetching scans with cursor: $cursor")
        val response = refreshScans(cursor)
        pageInfo = response.pageInfo
        canPaginate = response.pageInfo.cursor != null && response.results.size == PAGE_SIZE_LIMIT
        listState = ListState.LOADED

        cleanUploads()
    }

    private suspend fun refreshScans(cursor: String? = null): Paginated<Scan> {
        val userEmail = settingsService.getUserData().email
        Log.i("ScanListViewModel", "Fetching Scans for $userEmail")
        try {
            return scanClient.getScans(
                token = userEmail,
                limit = PAGE_SIZE_LIMIT,
                cursor = cursor,
                order = SortOrder.DESCENDING
            ).fold(
                onFailure = {
                    Log.e("ScanListViewModel", "Error: $it")
                    return@fold Paginated<Scan>(
                        results = emptyList(),
                        pageInfo = PageInfo(cursor = null)
                    )
                },
                onSuccess = { response ->
                    Log.i("ScanListViewModel", "Total Scans: ${response.results.size}")
                    pageInfo = response.pageInfo
                    hasScanned = if (userEmail.isNotEmpty()) {
                        _state.update { State.Data(parseScanList(response.results.toList())) }
                        response.results.isNotEmpty()
                    } else {
                        _state.update { State.Data(emptyList()) }
                        false
                    }
                    return response
                }
            )
        } catch (e: Exception) {
            Log.e("ScanListViewModel", "Error fetching scans: $e")
            return Paginated(
                results = emptyList(),
                pageInfo = PageInfo(cursor = null)
            )
        }
    }

    private fun parseScanList(newScans: List<Scan>): List<GroupedScan> {
        val currentScans = scans.list()
        val scans = newScans.toMutableSet()
        currentScans.forEach { currentScan ->
            if (!scans.map { it.id }.contains(currentScan.id)) {
                scans.add(currentScan)
            }
        }

        // We need to `toSet()` it again to ensure all duplicates are removed.
        return scans
            .toSet()
            .toList()
            .sortedByDescending { it.createdAt }
            .grouped()
            .sortedByDescending { it.date }
    }

    private fun cleanUploads() {
        // TODO: Implement File Cleaning
    }

    private fun resetScanList() {
        _state.value = State.Data(emptyList())
        pageInfo = null
    }

    fun createNewScan(file: File) {
        viewModelScope.launch {
            val userData = settingsService.getUserData()
            val scan = scanClient.createScan(NewScan(
                userToken = userData.email.lowercase(),
                deviceConfigName = "ANDROID_SCANNER"
            )).fold(
                onFailure = {
                    Log.e("ScanListViewModel", "Error: $it")
                    return@fold null
                },
                onSuccess = {
                    Log.i("ScanListViewModel", "Created Scan: $it")
                    hasScanned = true
                    lastScanId = it.id
                    return@fold it
                }
            ) ?: return@launch

            scanClient.uploadUrl(scan.id).fold(
                onFailure = {
                    Log.e("ScanListViewModel", "Error: $it")
                    return@fold
                },
                onSuccess = {
                    Log.i("ScanListViewModel", "Upload URL: $it")

                    // Rename the Zip
                    Log.i("ScanListViewModel", "Renaming file ${file.name}.zip to: scan_${scan.id}.zip")
                    val newFile = File(file.parent, "scan_${scan.id}.zip")
                    file.renameTo(newFile)

                    // 2. Upload the Zip
                    uploader.upload(file = newFile, url = it.url)

                    // 3. Refresh Scans when uploading is done
                    refreshScans()
                    return@fold
                }
            )
        }
    }
}
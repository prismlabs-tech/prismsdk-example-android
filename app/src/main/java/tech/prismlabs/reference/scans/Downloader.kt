package tech.prismlabs.reference.scans

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File

sealed class DownloadResult {
    data object Success : DownloadResult()
    data class Error(val message: String, val cause: Exception? = null) : DownloadResult()
    data class Progress(val progress: Int): DownloadResult()
}

class Downloader {
    private val client = HttpClient(Android.create())

    suspend fun download(url: String, location: File) {
        client.downloadFile(location, url) { success ->
            if (success) {
                println("Downloaded file to ${location.absolutePath}")
            } else {
                println("Failed to download file from $url")
            }
        }
    }
}

suspend fun HttpClient.downloadFile(file: File, url: String, callback: suspend (boolean: Boolean) -> Unit) {
    val call = this.request {
        url(url)
        method = HttpMethod.Get
    }
    if (!call.status.isSuccess()) {
        callback(false)
    }
    call.bodyAsChannel().copyAndClose(file.writeChannel())
    callback(true)
}


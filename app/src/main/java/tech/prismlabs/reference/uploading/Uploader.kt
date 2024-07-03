package tech.prismlabs.reference.uploading

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class Uploader {
    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress = _uploadProgress.asStateFlow()

    suspend fun upload(file: File, url: String) {
        Log.i("Uploader", "Preparing to upload file: ${file.name}")
        if (file.exists()) {
            Log.i("Uploader", "Uploading File: ${file.name}")
            try {
                val response = uploadFile(
                    url = url,
                    body = file.readBytes()
                )
                if (response.status.isSuccess()) {
                    Log.i("Uploader", "Uploaded: ${file.name}")
                    file.delete()
                } else {
                    Log.e("Uploader", "Failed Uploading: ${file.name} Status: ${response.status}")
                }
            } catch(e: Throwable) {
                Log.e("Uploader", "Failed Uploading: ${file.name}")
                e.printStackTrace()
            }
        } else {
            Log.e("Uploader", "Invalid file. The ${file.absolutePath} does not exist")
        }
    }

    private suspend fun uploadFile(url: String, body: ByteArray): HttpResponse {
        val client = HttpClient(Android) {
            install(HttpTimeout)
        }
        return client.put(url) {
            contentType(ContentType.Application.Zip)
            timeout { this.connectTimeoutMillis = 5000 }
            setBody(body)
            onUpload { bytesSentTotal, contentLength ->
                val progress = ((bytesSentTotal.toDouble() / contentLength.toDouble()) * 100).toInt()
                _uploadProgress.update { progress }
                Log.i("Uploader", "Uploading: $bytesSentTotal of $contentLength ($progress%)")
            }
        }
    }

//    private fun requestBuilder(body: ByteArray): (builder: HttpRequestBuilder) -> Unit {
//        return {
//            it.contentType(ContentType.Application.Zip)
//            it.timeout { this.connectTimeoutMillis = 5000 }
//            it.setBody(body)
//            it.onUpload { bytesSentTotal, contentLength ->
//                val progress = ((bytesSentTotal.toDouble() / contentLength.toDouble()) * 100).toInt()
//                _uploadProgress.update { progress }
//                Log.i("Uploader", "Uploading: $bytesSentTotal of $contentLength ($progress%)")
//            }
//        }
//    }
}
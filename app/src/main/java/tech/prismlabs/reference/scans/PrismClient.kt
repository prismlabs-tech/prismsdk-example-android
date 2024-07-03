package tech.prismlabs.reference.scans

import tech.prismlabs.prismsdk.api_client.ApiClient
import tech.prismlabs.reference.BuildConfig

class PrismClient {
    companion object {
        @Volatile private var shared: ApiClient? = null

        fun getSharedInstance(): ApiClient {
            return shared ?: synchronized(this) { // synchronized to avoid concurrency problem
                shared ?: ApiClient(
                    baseURL = BuildConfig.BASE_URL,
                    clientCredentials = BuildConfig.API_KEY
                ).also { shared = it }
            }
        }
    }
}
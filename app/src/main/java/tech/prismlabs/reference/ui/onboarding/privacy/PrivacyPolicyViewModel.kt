package tech.prismlabs.reference.ui.onboarding.privacy

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.prismlabs.prismsdk.api_client.UserClient
import tech.prismlabs.reference.scans.PrismClient
import tech.prismlabs.reference.settings.SettingsService
import tech.prismlabs.reference.settings.toExistingPrismUser

@OptIn(DelicateCoroutinesApi::class)
class PrivacyPolicyViewModel(
    private val settingsService: SettingsService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private var _valid = MutableStateFlow(false)
    val isValid = _valid.asStateFlow()

    var agreeToPrivacyPolicy by mutableStateOf(false)
        private set
    fun updateAgreeToPrivacyPolicy(value: Boolean) {
        agreeToPrivacyPolicy = value
        validate()
    }

    var agreeToSharingWithPrism by mutableStateOf(true)
        private set
    fun updateAgreeToSharingWithPrism(value: Boolean) {
        agreeToSharingWithPrism = value
        validate()
    }

    private fun validate() {
        if (agreeToPrivacyPolicy) {
            _valid.update { true }
        } else {
            _valid.update { false }
        }
    }

    suspend fun save() {
        Log.i("PrivacyPolicyViewModel", "Saving: $agreeToPrivacyPolicy, $agreeToSharingWithPrism")

        settingsService.updateTermsAndConditions(agreeToPrivacyPolicy)
        settingsService.updateSharingWithPrism(agreeToSharingWithPrism)

        val user = settingsService.getUserData()

        val client = UserClient(PrismClient.getSharedInstance())
        client.updateUser(user.toExistingPrismUser(researchConsent = agreeToSharingWithPrism))
    }

    init {
        CoroutineScope(dispatcher).launch {
            val agreedToTerms = settingsService.didAgreeToTerms()
            val shareWithPrism = settingsService.getSharingWithPrism()

            Log.i("PrivacyPolicyViewModel", "Loaded data")
            agreeToPrivacyPolicy = agreedToTerms
            agreeToSharingWithPrism = shareWithPrism
        }
    }
}

class PrivacyPolicyViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrivacyPolicyViewModel::class.java)) {
            return PrivacyPolicyViewModel(SettingsService(context)) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}
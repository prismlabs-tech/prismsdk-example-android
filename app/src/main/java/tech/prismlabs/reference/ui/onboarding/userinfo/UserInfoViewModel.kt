package tech.prismlabs.reference.ui.onboarding.userinfo

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import tech.prismlabs.reference.settings.UserData
import tech.prismlabs.reference.settings.toNewPrismUser

@OptIn(DelicateCoroutinesApi::class)
class UserInfoViewModel(
    private val settingsService: SettingsService,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private var _valid = MutableStateFlow(false)
    val isValid = _valid.asStateFlow()

    var email by mutableStateOf("")
        private set
    fun updateEmail(value: String) {
        email = value
        validate()
    }
    private val isEmailValid: Boolean get() {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.matches(emailRegex)
    }

    var sex by mutableIntStateOf(0)
        private set
    fun updateSex(value: Int) {
        sex = value
        validate()
    }

    var height by mutableIntStateOf(69)
        private set
    fun updateHeight(value: Int) {
        height = value
        validate()
    }

    var weight by mutableIntStateOf(167)
        private set
    fun updateWeight(value: Int) {
        weight = value
        validate()
    }

    var age by mutableIntStateOf(42)
        private set
    fun updateAge(value: Int) {
        age = value
        validate()
    }

    var show3dModel by mutableStateOf(true)
        private set
    fun updateShow3dModel(value: Boolean) {
        show3dModel = value
        validate()
    }

    private fun validate() {
        if (email.isEmpty() && !isEmailValid) {
            _valid.update { false }
            return
        }
        if (height <= 0) {
            _valid.update { false }
            return
        }
        if (weight <= 0) {
            _valid.update { false }
            return
        }
        if (age <= 0) {
            _valid.update { false }
            return
        }
        _valid.update { true }
    }

    suspend fun save() {
        // Save user info
        val userData = UserData(
            email = email,
            sex = sex,
            height = height,
            weight = weight,
            age = age,
            show3dModel = show3dModel
        )
        Log.i("UserInfoViewModel", "Saving user data: $userData")
        settingsService.saveUserData(userData)

        // At the current onboarding state, we want reset the privacy policy checkbox
        // We are forcing this to be false so when the screen is shown again, the user
        // will have to agree to the terms again.
        settingsService.updateTermsAndConditions(false)

        val client = UserClient(PrismClient.getSharedInstance())
        client.createUser(userData.toNewPrismUser())
    }

    init {
        CoroutineScope(dispatcher).launch {
            val userData = settingsService.getUserData()
            Log.i("UserInfoViewModel", "Loaded user data: $userData")
            email = userData.email
            sex = userData.sex
            height = userData.height
            weight = userData.weight
            age = userData.age
            show3dModel = userData.show3dModel
            validate()
        }
    }
}

class UserInfoViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(SettingsService(context)) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}
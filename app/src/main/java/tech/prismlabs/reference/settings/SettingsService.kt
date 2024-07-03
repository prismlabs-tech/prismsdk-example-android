package tech.prismlabs.reference.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsService(private val context: Context) {
    companion object {
        val EMAIL = stringPreferencesKey("email")
        val SEX = intPreferencesKey("sex")
        val HEIGHT = intPreferencesKey("height")
        val WEIGHT = intPreferencesKey("weight")
        val AGE = intPreferencesKey("age")
        val SHOW_3D_MODEL = booleanPreferencesKey("show_3d_model")

        val AGREE_TO_TERMS = booleanPreferencesKey("agree_to_terms")
        val AGREE_TO_SHARING_WITH_PRISM = booleanPreferencesKey("agree_to_sharing_with_prism")
    }

    private val _onboardingComplete = MutableSharedFlow<Boolean>()
    val onboardingComplete = _onboardingComplete.asSharedFlow().distinctUntilChanged()

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun saveUserData(userData: UserData) {
        context.dataStore.edit { settings ->
            settings[EMAIL] = userData.email
            settings[SEX] = userData.sex
            settings[HEIGHT] = userData.height
            settings[WEIGHT] = userData.weight
            settings[AGE] = userData.age
            settings[SHOW_3D_MODEL] = userData.show3dModel
        }
        onboardingCompleteCheck()
    }

    suspend fun getUserData(): UserData {
        val settings = context.dataStore.data.first()
        return UserData(
            email = settings[EMAIL] ?: "",
            sex = settings[SEX] ?: 0,
            height = settings[HEIGHT] ?: 69,
            weight = settings[WEIGHT] ?: 167,
            age = settings[AGE] ?: 42,
            show3dModel = settings[SHOW_3D_MODEL] ?: true
        )
    }

    suspend fun updateTermsAndConditions(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[AGREE_TO_TERMS] = value
        }
        onboardingCompleteCheck()
    }

    suspend fun didAgreeToTerms(): Boolean {
        val settings = context.dataStore.data.first()
        return settings[AGREE_TO_TERMS] ?: false
    }

    suspend fun updateSharingWithPrism(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[AGREE_TO_SHARING_WITH_PRISM] = value
        }
        onboardingCompleteCheck()
    }

    suspend fun getSharingWithPrism(): Boolean {
        val settings = context.dataStore.data.first()
        return settings[AGREE_TO_SHARING_WITH_PRISM] ?: true
    }

    suspend fun checkOnboardingComplete() {
        onboardingCompleteCheck()
    }

    private suspend fun onboardingCompleteCheck() {
        // Uncomment this line to reset the onboarding state
        // context.dataStore.edit {  it.clear() }

        val settings = context.dataStore.data.first()
        val agreedToTerms = settings[AGREE_TO_TERMS] ?: false
        val hasEmail = settings[AGREE_TO_SHARING_WITH_PRISM] != null

        if (agreedToTerms && hasEmail) {
            _onboardingComplete.emit(true)
        } else {
            _onboardingComplete.emit(false)
        }
    }
}
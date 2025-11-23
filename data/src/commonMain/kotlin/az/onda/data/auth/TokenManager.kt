package az.onda.data.auth

import az.onda.data.api.model.TokenResponse
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.russhwolf.settings.get

class TokenManager(private val settings: Settings = Settings()) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveTokens(tokenResponse: TokenResponse) {
        settings[KEY_ACCESS_TOKEN] = tokenResponse.accessToken
        settings[KEY_REFRESH_TOKEN] = tokenResponse.refreshToken
        settings[KEY_EXPIRES_AT] = tokenResponse.expiresAt
    }

    fun saveUserId(userId: String) {
        settings[KEY_USER_ID] = userId
    }

    fun getAccessToken(): String? {
        return settings[KEY_ACCESS_TOKEN]
    }

    fun getRefreshToken(): String? {
        return settings[KEY_REFRESH_TOKEN]
    }

    fun getExpiresAt(): String? {
        return settings[KEY_EXPIRES_AT]
    }

    fun getUserId(): String? {
        return settings[KEY_USER_ID]
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null && getRefreshToken() != null
    }

    fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_EXPIRES_AT)
        settings.remove(KEY_USER_ID)
    }
}

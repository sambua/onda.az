package az.onda.data.auth

import az.onda.data.api.AuthApi
import az.onda.data.api.model.OtpChannel
import az.onda.data.api.model.OtpSendRequest
import az.onda.data.api.model.OtpVerifyRequest
import az.onda.data.api.model.RefreshTokenRequest
import az.onda.data.api.model.SocialAuthRequest
import az.onda.data.api.model.SocialProvider
import az.onda.data.api.model.UserResponse
import az.onda.shared.util.RequestState

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    override fun getCurrentUserId(): String? {
        return tokenManager.getUserId()
    }

    override suspend fun signInWithGoogle(idToken: String): RequestState<UserResponse> {
        return signInWithSocial(SocialProvider.GOOGLE, idToken)
    }

    override suspend fun signInWithApple(idToken: String): RequestState<UserResponse> {
        return signInWithSocial(SocialProvider.APPLE, idToken)
    }

    private suspend fun signInWithSocial(
        provider: SocialProvider,
        token: String
    ): RequestState<UserResponse> {
        val request = SocialAuthRequest(provider = provider, token = token)
        return authApi.socialAuth(request).fold(
            onSuccess = { tokenResponse ->
                tokenManager.saveTokens(tokenResponse)
                fetchAndSaveUser()
            },
            onFailure = { error ->
                RequestState.Error(error.message ?: "Social auth failed")
            }
        )
    }

    override suspend fun sendOtp(phone: String, channel: OtpChannel): RequestState<Unit> {
        val request = OtpSendRequest(channel = channel, phone = phone)
        return authApi.sendOtp(request).fold(
            onSuccess = {
                RequestState.Success(Unit)
            },
            onFailure = { error ->
                RequestState.Error(error.message ?: "Failed to send OTP")
            }
        )
    }

    override suspend fun verifyOtp(phone: String, code: String): RequestState<UserResponse> {
        val request = OtpVerifyRequest(code = code, phone = phone)
        return authApi.verifyOtp(request).fold(
            onSuccess = { tokenResponse ->
                tokenManager.saveTokens(tokenResponse)
                fetchAndSaveUser()
            },
            onFailure = { error ->
                RequestState.Error(error.message ?: "OTP verification failed")
            }
        )
    }

    override suspend fun getCurrentUser(): RequestState<UserResponse> {
        val accessToken = tokenManager.getAccessToken()
            ?: return RequestState.Error("Not authenticated")

        return authApi.getCurrentUser(accessToken).fold(
            onSuccess = { user ->
                tokenManager.saveUserId(user.id)
                RequestState.Success(user)
            },
            onFailure = { error ->
                RequestState.Error(error.message ?: "Failed to get user")
            }
        )
    }

    override suspend fun refreshToken(): RequestState<Unit> {
        val refreshToken = tokenManager.getRefreshToken()
            ?: return RequestState.Error("No refresh token")

        val request = RefreshTokenRequest(refreshToken = refreshToken)
        return authApi.refreshToken(request).fold(
            onSuccess = { tokenResponse ->
                tokenManager.saveTokens(tokenResponse)
                RequestState.Success(Unit)
            },
            onFailure = { error ->
                tokenManager.clearTokens()
                RequestState.Error(error.message ?: "Token refresh failed")
            }
        )
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            tokenManager.clearTokens()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Sign out failed: ${e.message}")
        }
    }

    private suspend fun fetchAndSaveUser(): RequestState<UserResponse> {
        return getCurrentUser()
    }
}

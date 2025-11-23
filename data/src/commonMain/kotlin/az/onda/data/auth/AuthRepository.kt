package az.onda.data.auth

import az.onda.data.api.model.OtpChannel
import az.onda.data.api.model.SocialProvider
import az.onda.data.api.model.UserResponse
import az.onda.shared.util.RequestState

interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun getCurrentUserId(): String?

    suspend fun signInWithGoogle(idToken: String): RequestState<UserResponse>
    suspend fun signInWithApple(idToken: String): RequestState<UserResponse>

    suspend fun sendOtp(phone: String, channel: OtpChannel): RequestState<Unit>
    suspend fun verifyOtp(phone: String, code: String): RequestState<UserResponse>

    suspend fun getCurrentUser(): RequestState<UserResponse>
    suspend fun refreshToken(): RequestState<Unit>
    suspend fun signOut(): RequestState<Unit>
}

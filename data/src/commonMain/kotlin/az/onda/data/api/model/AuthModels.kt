package az.onda.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============ Enums ============

@Serializable
enum class OtpChannel {
    @SerialName("sms") SMS,
    @SerialName("whatsapp") WHATSAPP,
    @SerialName("telegram") TELEGRAM
}

@Serializable
enum class UserType {
    @SerialName("CUSTOMER") CUSTOMER,
    @SerialName("BUSINESS") BUSINESS,
    @SerialName("PARTNER") PARTNER,
    @SerialName("ADMIN") ADMIN
}

@Serializable
enum class SocialProvider {
    @SerialName("google") GOOGLE,
    @SerialName("apple") APPLE
}

// ============ Request Models ============

@Serializable
data class OtpSendRequest(
    val channel: OtpChannel,
    val phone: String
)

@Serializable
data class OtpVerifyRequest(
    val code: String,
    val phone: String,
    @SerialName("user_type") val userType: UserType = UserType.CUSTOMER
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val phone: String = "",
    val role: String = "general",
    @SerialName("user_type") val userType: UserType = UserType.CUSTOMER
)

@Serializable
data class SocialAuthRequest(
    val provider: SocialProvider,
    val token: String,
    @SerialName("user_type") val userType: UserType = UserType.CUSTOMER
)

// ============ Response Models ============

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_at") val expiresAt: String,
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class OtpSendResponse(
    val success: Boolean = true,
    @SerialName("expires_in") val expiresIn: Int = 300,
    @SerialName("retry_after") val retryAfter: Int = 60
)

@Serializable
data class UserResponse(
    @SerialName("ID") val id: String,
    @SerialName("Email") val email: String,
    @SerialName("Phone") val phone: String,
    @SerialName("UserType") val userType: String,
    @SerialName("Role") val role: String,
    @SerialName("EmailVerified") val emailVerified: Boolean,
    @SerialName("PhoneVerified") val phoneVerified: Boolean
)

// ============ Error Response ============

@Serializable
data class ApiError(
    val message: String
)

@Serializable
data class ApiErrorResponse(
    val success: Boolean = false,
    val errors: List<ApiError> = emptyList()
) {
    fun getErrorMessage(): String = errors.firstOrNull()?.message ?: "Unknown error"
}

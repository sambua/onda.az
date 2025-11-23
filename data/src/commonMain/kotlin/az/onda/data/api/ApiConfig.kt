package az.onda.data.api

object ApiConfig {
    // Toggle this for development vs production
    private const val IS_DEVELOPMENT = true

    val BASE_URL: String
        get() = if (IS_DEVELOPMENT) {
            "http://localhost:8084/api/v1"
        } else {
            "https://api.onda.az"
        }

    object Endpoints {
        const val OTP_SEND = "/auth/otp/send"
        const val OTP_VERIFY = "/auth/otp/verify"
        const val REFRESH = "/auth/refresh"
        const val REGISTER = "/auth/register"
        const val SOCIAL = "/auth/social"
        const val ME = "/auth/me"
    }
}

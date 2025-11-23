package az.onda.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Auth: Screen()
    @Serializable
    data class OtpVerification(val phone: String = "", val channel: String = "SMS"): Screen()
    @Serializable
    data object HomeGraph: Screen()
    @Serializable
    data object Reservation: Screen()
    @Serializable
    data object Profile: Screen()
    @Serializable
    data object Categories: Screen()
}
package az.onda.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Customer (
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val accessToken: String? = null,
    val image: String? = null,
    val mobile: String? = null,
    val city: String? = null,
    val phoneNumber: PhoneNumber? = null,
    val reservations: List<Reservation> = emptyList()
)

@Serializable
data class PhoneNumber(
    val countryCode: Int,
    val number: String
) {
    override fun toString(): String {
        return "+$countryCode $number"
    }
}
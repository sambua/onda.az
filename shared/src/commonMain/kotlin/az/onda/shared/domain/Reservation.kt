package az.onda.shared.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class Reservation constructor(
    val id: String = Uuid.random().toHexString(),
    val executorID: String,
    val price: Double = 0.0,
    val currency: String = "AZN",
)

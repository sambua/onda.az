package az.onda.home.domain

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeTab {
    @Serializable
    data object Feed : HomeTab()
    @Serializable
    data object Categories : HomeTab()
    @Serializable
    data object Reservations : HomeTab()
    @Serializable
    data object Profile : HomeTab()
}

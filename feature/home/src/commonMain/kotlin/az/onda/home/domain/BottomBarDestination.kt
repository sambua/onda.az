package az.onda.home.domain

import az.onda.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val title: String,
    val tab: HomeTab,
) {
    Home(
        icon = Resources.Icon.Eye,
        title = "Home",
        tab = HomeTab.Feed
    ),
    Search(
        icon = Resources.Icon.Search,
        title = "Search",
        tab = HomeTab.Categories
    ),
    Reservation(
        icon = Resources.Icon.Deadline,
        title = "Reservation",
        tab = HomeTab.Reservations
    ),
    Profile(
        icon = Resources.Icon.Profile,
        title = "Profile",
        tab = HomeTab.Profile
    )
}
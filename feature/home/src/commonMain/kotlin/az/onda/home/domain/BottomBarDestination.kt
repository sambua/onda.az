package az.onda.home.domain

import androidx.compose.material3.Icon
import az.onda.shared.Resources
import az.onda.shared.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val route: String,
    val title: String,
    val screen: Screen,
) {
    Home(
        icon = Resources.Icon.Eye,
        route = "home_route",
        title = "Home",
        screen = Screen.HomeGraph
    ),
    Search(
        icon = Resources.Icon.Search,
        route = "search_route",
        title = "Search",
        screen = Screen.Categories
    ),
    Reservation(
        icon = Resources.Icon.Deadline,
        route = "reservation_route",
        title = "Reservation",
        screen = Screen.Reservation
    ),
    Profile(
        icon = Resources.Icon.Profile,
        route = "profile_route",
        title = "Profile",
        screen = Screen.Profile
    )
}
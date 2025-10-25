package az.onda.home.domain

import az.onda.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class DrawerItem (
    val title: String,
    val icon: DrawableResource
) {
    Home(
        title = "Home",
        icon = Resources.Icon.Home
    ),
    Profile(
        title = "Profile",
        icon = Resources.Icon.Profile
    ),
    Reservations(
        title = "Reservations",
        icon = Resources.Icon.Deadline
    ),
    Notifications(
        title = "Notifications",
        icon = Resources.Icon.Alert
    ),
    Contacts(
        title = "Contacts",
        icon = Resources.Icon.Contacts
    ),
    SignOut(
        title = "Sign Out",
        icon = Resources.Icon.SignOut
    ),
    Admin(
        title = "Sign Out",
        icon = Resources.Icon.Admin
    )
}
package az.onda.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.onda.home.domain.DrawerItem
import az.onda.shared.FontSizes
import az.onda.shared.robotoCondensedFont
import az.onda.shared.TextSecondary

@Composable
fun CustomDrawer(
    onProfileClick: () -> Unit,
    onReservationsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onContactsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onAdminPanelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .padding(horizontal = 12.dp),
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Onda",
            color = TextSecondary,
            fontFamily = robotoCondensedFont(),
            fontSize= FontSizes.EXTRA_LARGE
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "saat, 10-da",
            color = TextSecondary,
            fontFamily = robotoCondensedFont(),
            fontSize= FontSizes.REGULAR
        )
        Spacer(modifier = Modifier.height(50.dp))
        DrawerItem.entries.take(DrawerItem.entries.size - 1).forEach { item ->
            DrawerItemCard(
                drawerItem = item,
                onItemClick = {
                    when (item) {
                        DrawerItem.Profile -> onProfileClick()
                        DrawerItem.Reservations -> onReservationsClick()
                        DrawerItem.Notifications -> onNotificationsClick()
                        DrawerItem.Contacts -> onContactsClick()
                        DrawerItem.SignOut -> onSignOutClick()
                        else -> {}
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        DrawerItemCard(
            drawerItem = DrawerItem.Admin,
            onItemClick = {
                onAdminPanelClick()
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
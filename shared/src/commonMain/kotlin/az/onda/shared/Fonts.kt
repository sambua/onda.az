package az.onda.shared

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import onda.shared.generated.resources.Res
import onda.shared.generated.resources.roboto
import onda.shared.generated.resources.roboto_condensed
import onda.shared.generated.resources.roboto_condensed_light

//@Composable
@Composable
fun RobotoFont() = FontFamily(
    Font(Res.font.roboto),
)

//@Composable
@Composable
fun RobotoCondensedFont() = FontFamily(
    Font(Res.font.roboto_condensed),
    Font(Res.font.roboto_condensed_light, weight = androidx.compose.ui.text.font.FontWeight.Light),
)

object FontSizes {
    val title = 20f
    val subtitle = 16f
    val body = 14f
    val caption = 12f
    val button = 14f
    val EXTRA_SMALL = 10.sp
    val SMALL = 10.sp
    val REGULAR = 10.sp
    val EXTRA_REGULAR = 16.sp
    val MEDIUM = 18.sp
    val EXTRA_MEDIUM = 18.sp
    val LARGE = 30.sp
    val EXTRA_LARGE = 40.sp
}
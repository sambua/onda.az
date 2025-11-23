package az.onda.auth.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import az.onda.shared.Black
import az.onda.shared.FontSizes
import az.onda.shared.Resources
import az.onda.shared.White
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppleButton(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    primaryText: String = "Sign in with Apple",
    loadingText: String = "Signing in...",
    shape: Shape = RoundedCornerShape(8.dp),
    borderStrokeWidth: Dp = 1.dp,
    backgroundColor: Color = Black,
    contentColor: Color = White,
    progressIndicatorColor: Color = White,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = { if (!loading) onClick() },
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = progressIndicatorColor,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    painter = painterResource(Resources.Image.AppleLogo),
                    contentDescription = "Apple Logo",
                    modifier = Modifier.size(20.dp),
                    tint = contentColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (loading) loadingText else primaryText,
                fontSize = FontSizes.REGULAR,
                color = contentColor
            )
        }
    }
}

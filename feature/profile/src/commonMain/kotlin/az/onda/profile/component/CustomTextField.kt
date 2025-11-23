package az.onda.profile.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import az.onda.shared.Alpha
import az.onda.shared.BorderError
import az.onda.shared.BorderIdle
import az.onda.shared.FontSizes
import az.onda.shared.IconSecondary
import az.onda.shared.SurfaceDarker
import az.onda.shared.SurfaceLighter
import az.onda.shared.TextPrimary

@Composable
fun CustomTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    )
) {
    val borderColor = animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor.value,
                shape = RoundedCornerShape(size = 6.dp)
            ),
        value = value,
        onValueChange = onValueChange,
        placeholder = if (placeholder != null) {
            {
                Text(
                    modifier = Modifier.alpha(Alpha.HALF),
                    text = placeholder,
                    fontSize = FontSizes.REGULAR
                )
            }
        } else null,
        enabled = enabled,
        singleLine = !expanded,
        shape = RoundedCornerShape(size = 6.dp),
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = SurfaceLighter,
            focusedContainerColor = SurfaceLighter,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            focusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
            unfocusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
            disabledPlaceholderColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            disabledContainerColor = SurfaceDarker,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            selectionColors = TextSelectionColors(
                handleColor = IconSecondary,
                backgroundColor = Color.Unspecified
            )
        )
    )

}
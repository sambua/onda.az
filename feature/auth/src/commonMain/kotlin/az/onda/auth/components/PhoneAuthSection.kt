package az.onda.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import az.onda.data.api.model.OtpChannel
import az.onda.shared.Black
import az.onda.shared.FontSizes
import az.onda.shared.Gray
import az.onda.shared.Greenish
import az.onda.shared.Resources
import az.onda.shared.White
import org.jetbrains.compose.resources.painterResource

data class CountryCode(
    val code: String,
    val country: String,
    val flag: String
)

val countryCodes = listOf(
    CountryCode("+994", "Azerbaijan", "\uD83C\uDDE6\uD83C\uDDFF"),
    CountryCode("+90", "Turkey", "\uD83C\uDDF9\uD83C\uDDF7"),
    CountryCode("+7", "Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
    CountryCode("+1", "USA", "\uD83C\uDDFA\uD83C\uDDF8"),
    CountryCode("+44", "UK", "\uD83C\uDDEC\uD83C\uDDE7"),
    CountryCode("+49", "Germany", "\uD83C\uDDE9\uD83C\uDDEA"),
    CountryCode("+33", "France", "\uD83C\uDDEB\uD83C\uDDF7"),
    CountryCode("+39", "Italy", "\uD83C\uDDEE\uD83C\uDDF9"),
    CountryCode("+34", "Spain", "\uD83C\uDDEA\uD83C\uDDF8"),
    CountryCode("+971", "UAE", "\uD83C\uDDE6\uD83C\uDDEA"),
)

@Composable
fun PhoneAuthSection(
    modifier: Modifier = Modifier,
    countryCode: String,
    onCountryCodeChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    selectedChannel: OtpChannel,
    isLoading: Boolean,
    onSendOtp: (OtpChannel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCountry = remember(countryCode) {
        countryCodes.find { it.code == countryCode } ?: countryCodes.first()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Country Code Selector
            Column {
                Surface(
                    modifier = Modifier
                        .width(100.dp)
                        .height(56.dp),
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(8.dp),
                    color = White,
                    border = BorderStroke(1.dp, Gray)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = selectedCountry.flag,
                            fontSize = FontSizes.LARGE
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedCountry.code,
                            fontSize = FontSizes.REGULAR,
                            color = Black
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countryCodes.forEach { country ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = country.flag)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = country.code)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = country.country,
                                        color = Gray,
                                        fontSize = FontSizes.SMALL
                                    )
                                }
                            },
                            onClick = {
                                onCountryCodeChange(country.code)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Phone Number Field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { onPhoneChange(it.filter { char -> char.isDigit() }) },
                modifier = Modifier.weight(1f),
                label = { Text("Phone Number") },
                placeholder = { Text("501234567") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Greenish,
                    unfocusedBorderColor = Gray,
                    focusedLabelColor = Greenish,
                    cursorColor = Greenish
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Choose verification method",
            fontSize = FontSizes.SMALL,
            color = Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChannelButton(
                modifier = Modifier.weight(1f),
                channel = OtpChannel.SMS,
                isSelected = selectedChannel == OtpChannel.SMS,
                isLoading = isLoading && selectedChannel == OtpChannel.SMS,
                enabled = phoneNumber.length >= 9,
                onClick = { onSendOtp(OtpChannel.SMS) }
            )
            ChannelButton(
                modifier = Modifier.weight(1f),
                channel = OtpChannel.WHATSAPP,
                isSelected = selectedChannel == OtpChannel.WHATSAPP,
                isLoading = isLoading && selectedChannel == OtpChannel.WHATSAPP,
                enabled = phoneNumber.length >= 9,
                onClick = { onSendOtp(OtpChannel.WHATSAPP) }
            )
            ChannelButton(
                modifier = Modifier.weight(1f),
                channel = OtpChannel.TELEGRAM,
                isSelected = selectedChannel == OtpChannel.TELEGRAM,
                isLoading = isLoading && selectedChannel == OtpChannel.TELEGRAM,
                enabled = phoneNumber.length >= 9,
                onClick = { onSendOtp(OtpChannel.TELEGRAM) }
            )
        }
    }
}

@Composable
private fun ChannelButton(
    modifier: Modifier = Modifier,
    channel: OtpChannel,
    isSelected: Boolean,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Greenish else White
    val contentColor = if (isSelected) White else Black
    val borderColor = if (isSelected) Greenish else Gray

    Surface(
        modifier = modifier.height(56.dp),
        onClick = { if (enabled && !isLoading) onClick() },
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    painter = painterResource(
                        when (channel) {
                            OtpChannel.SMS -> Resources.Icon.Sms
                            OtpChannel.WHATSAPP -> Resources.Icon.WhatsApp
                            OtpChannel.TELEGRAM -> Resources.Icon.Telegram
                        }
                    ),
                    contentDescription = channel.name,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (channel) {
                    OtpChannel.SMS -> "SMS"
                    OtpChannel.WHATSAPP -> "WhatsApp"
                    OtpChannel.TELEGRAM -> "Telegram"
                },
                fontSize = FontSizes.EXTRA_SMALL,
                color = contentColor
            )
        }
    }
}

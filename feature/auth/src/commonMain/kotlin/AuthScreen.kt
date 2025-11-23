package az.onda.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.onda.auth.components.GoogleButton
import az.onda.auth.components.PhoneAuthSection
import az.onda.data.api.model.OtpChannel
import az.onda.shared.Black
import az.onda.shared.Error
import az.onda.shared.FontSizes
import az.onda.shared.Gray
import az.onda.shared.Greenish
import az.onda.shared.White
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import ContentWithMessageBar
import rememberMessageBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    navigateToHome: () -> Unit,
    navigateToOtpVerification: (phone: String, channel: String) -> Unit = { _, _ -> }
) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val uiState = viewModel.uiState
    val messageBarState = rememberMessageBarState()

    var countryCode by remember { mutableStateOf("+994") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedChannel by remember { mutableStateOf(OtpChannel.SMS) }
    var googleLoading by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = White
    ) { padding ->
        ContentWithMessageBar(
            modifier = Modifier.padding(padding),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContainerColor = Error,
            errorContentColor = Black,
            successContainerColor = Greenish,
            successContentColor = Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "Welcome to Onda",
                    fontSize = FontSizes.EXTRA_LARGE,
                    color = Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in to continue",
                    fontSize = FontSizes.REGULAR,
                    color = Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Social Login Section
                GoogleButtonUiContainer(
                    onGoogleSignInResult = { googleUser ->
                        if (googleUser == null) {
                            googleLoading = false
                            messageBarState.addError("Google Sign-In failed.")
                            return@GoogleButtonUiContainer
                        }
                        viewModel.signInWithGoogle(
                            user = googleUser,
                            onSuccess = {
                                scope.launch {
                                    messageBarState.addSuccess("Sign-in successful!")
                                    delay(1000)
                                    navigateToHome()
                                }
                            },
                            onError = { error ->
                                googleLoading = false
                                messageBarState.addError(error)
                            }
                        )
                    }
                ) {
                    GoogleButton(
                        loading = googleLoading || uiState.isLoading,
                        onClick = {
                            googleLoading = true
                            this.onClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Divider with "or"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Gray.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "  or continue with phone  ",
                        fontSize = FontSizes.SMALL,
                        color = Gray
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Gray.copy(alpha = 0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Phone Auth Section
                PhoneAuthSection(
                    countryCode = countryCode,
                    onCountryCodeChange = { countryCode = it },
                    phoneNumber = phoneNumber,
                    onPhoneChange = { phoneNumber = it },
                    selectedChannel = selectedChannel,
                    isLoading = uiState.isLoading,
                    onSendOtp = { channel ->
                        selectedChannel = channel
                        val fullPhoneNumber = "$countryCode$phoneNumber"
                        viewModel.sendOtp(
                            phone = fullPhoneNumber,
                            channel = channel,
                            onSuccess = {
                                scope.launch {
                                    messageBarState.addSuccess("Verification code sent!")
                                    delay(500)
                                    navigateToOtpVerification(fullPhoneNumber, channel.name)
                                }
                            },
                            onError = { error ->
                                messageBarState.addError(error)
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                    fontSize = FontSizes.EXTRA_SMALL,
                    color = Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
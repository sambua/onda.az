package az.onda.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.onda.data.api.model.OtpChannel
import az.onda.shared.Black
import az.onda.shared.Error
import az.onda.shared.FontSizes
import az.onda.shared.Gray
import az.onda.shared.Greenish
import az.onda.shared.Resources
import az.onda.shared.White
import ContentWithMessageBar
import rememberMessageBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    phone: String,
    channel: String,
    onNavigateBack: () -> Unit,
    onVerificationSuccess: () -> Unit
) {
    val viewModel = koinViewModel<AuthViewModel>()
    val uiState = viewModel.uiState
    val scope = rememberCoroutineScope()
    val messageBarState = rememberMessageBarState()

    var otpCode by remember { mutableStateOf("") }
    var countdown by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val otpChannel = remember {
        OtpChannel.entries.find { it.name == channel } ?: OtpChannel.SMS
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown--
        } else {
            canResend = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verify Phone") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.Close),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        }
    ) { padding ->
        ContentWithMessageBar(
            modifier = Modifier.padding(padding),
            messageBarState = messageBarState,
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
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Enter verification code",
                    fontSize = FontSizes.LARGE,
                    color = Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We sent a code to $phone via ${otpChannel.name}",
                    fontSize = FontSizes.REGULAR,
                    color = Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { value ->
                        if (value.length <= 6 && value.all { it.isDigit() }) {
                            otpCode = value
                            if (value.length == 6) {
                                viewModel.verifyOtp(
                                    code = value,
                                    onSuccess = {
                                        scope.launch {
                                            messageBarState.addSuccess("Verification successful!")
                                            delay(1000)
                                            onVerificationSuccess()
                                        }
                                    },
                                    onError = { error ->
                                        messageBarState.addError(error)
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .focusRequester(focusRequester),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = FontSizes.EXTRA_LARGE
                    ),
                    placeholder = {
                        Text(
                            text = "000000",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Gray
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Greenish,
                        unfocusedBorderColor = Gray,
                        cursorColor = Greenish
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Greenish,
                        strokeWidth = 3.dp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Didn't receive code?",
                        fontSize = FontSizes.REGULAR,
                        color = Gray
                    )
                    if (canResend) {
                        TextButton(
                            onClick = {
                                viewModel.sendOtp(
                                    phone = phone,
                                    channel = otpChannel,
                                    onSuccess = {
                                        countdown = 60
                                        canResend = false
                                        scope.launch {
                                            messageBarState.addSuccess("Code sent again!")
                                        }
                                    },
                                    onError = { error ->
                                        messageBarState.addError(error)
                                    }
                                )
                            }
                        ) {
                            Text(
                                text = "Resend",
                                fontSize = FontSizes.REGULAR,
                                color = Greenish
                            )
                        }
                    } else {
                        Text(
                            text = " Resend in ${countdown}s",
                            fontSize = FontSizes.REGULAR,
                            color = Greenish
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

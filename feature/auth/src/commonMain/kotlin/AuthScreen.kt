import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.onda.auth.AuthViewModel
import az.onda.auth.components.GoogleButton
import az.onda.shared.Alpha
import az.onda.shared.Black
import az.onda.shared.Error
import az.onda.shared.FontSizes
import az.onda.shared.Gray
import az.onda.shared.Greenish
import az.onda.shared.RobotoFont
import az.onda.shared.White
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AuthScreen(
    navigateToHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val messageBarState = rememberMessageBarState()
    var loadingState by remember { mutableStateOf(false) }

    Scaffold { padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Gray,
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContainerColor = Error,
            errorContentColor = Black,
            successContainerColor = Greenish,
            successContentColor = Black,
        ) {
            Column (
                modifier = Modifier.fillMaxSize()
                    .padding(24.dp),
            ) {
                Column (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = "Auth Screen",
                        textAlign = TextAlign.Center,
                        fontFamily = RobotoFont(),
                        fontSize = FontSizes.EXTRA_LARGE,
                        color = Gray
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(Alpha.HALF),
                        text = "Login to proceed",
                        textAlign = TextAlign.Center,
                        fontSize = FontSizes.EXTRA_REGULAR,
                        color = Greenish
                    )
                }
                GoogleButtonUiContainer(
                    onGoogleSignInResult = { googleUser ->
                        if (googleUser == null) {
                            loadingState = false
                            messageBarState.addError("Google Sign-In failed.")
                            return@GoogleButtonUiContainer
                        }
                        viewModel.createCustomer(
                            user = googleUser,
                            onSuccess = {
                                scope.launch {
                                    messageBarState.addSuccess("Google Sign-In successful.")
                                    delay(1000)
                                    navigateToHome()
                                }
                            },
                            onError = { errorMsg -> messageBarState.addError("Google Sign-In failed: $errorMsg") }
                        )
                        loadingState = false
                    }
                ) {
                    GoogleButton(
                        loading = loadingState,
                        onClick = {
                            loadingState = true
                            this.onClick()
                        }
                    )
                }
            }
        }

    }

}
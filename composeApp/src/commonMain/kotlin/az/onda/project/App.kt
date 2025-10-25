package az.onda.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import az.onda.data.domain.CustomerRepository
import az.onda.navigation.SetupNavGraph
import az.onda.shared.navigation.Screen
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        val customerRepository = koinInject<CustomerRepository>()
        var appReady by remember { mutableStateOf(false) }
        val isUserAuthenticated = remember { customerRepository.getCurrentUserID() != null }
        val startDestination = remember {
            if (!isUserAuthenticated) {
                Screen.HomeGraph
            } else {
                Screen.Auth
            }
        }

        LaunchedEffect(Unit) {
            GoogleAuthProvider.create(
                credentials = GoogleAuthCredentials(
                    serverId = az.onda.shared.Constants.OAUTH_ID
                )
            )
            appReady = true
        }

        // SetupNavGraph()
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = appReady,
        ) {
            SetupNavGraph(
                startDestination = startDestination
            )
        }
    }
}
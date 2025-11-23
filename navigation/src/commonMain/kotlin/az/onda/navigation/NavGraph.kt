package az.onda.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import az.onda.auth.AuthScreen
import az.onda.auth.OtpVerificationScreen
import az.onda.home.HomeGraphScreen
import az.onda.shared.navigation.Screen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.Auth) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                },
                navigateToOtpVerification = { phone, channel ->
                    navController.navigate(Screen.OtpVerification(phone, channel))
                }
            )
        }
        composable<Screen.OtpVerification> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.OtpVerification>()
            // Handle edge case where arguments might be empty after process death
            if (route.phone.isEmpty()) {
                navController.popBackStack()
                return@composable
            }
            OtpVerificationScreen(
                phone = route.phone,
                channel = route.channel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onVerificationSuccess = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo<Screen.HomeGraph> { inclusive = true }
                    }
                }
            )
        }
    }
}
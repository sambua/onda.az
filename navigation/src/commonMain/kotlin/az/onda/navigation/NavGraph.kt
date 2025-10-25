package az.onda.navigation

import androidx.compose.runtime.Composable
// Importing the AuthScreen composable from the auth feature module
// import az.onda.auth.AuthScreen <<-- this one is not working for some reason
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable

import AuthScreen
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
                        // This will clear the back stack so user cannot navigate back to AuthScreen
                        popUpTo<Screen.Auth>{ inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                    // This will clear the back stack so user cannot navigate back to AuthScreen
                        popUpTo<Screen.HomeGraph>{ inclusive = true }
                    }
                }
            )
        }
    }
    // AuthScreen()
}
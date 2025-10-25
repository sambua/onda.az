package az.onda.home

import ContentWithMessageBar
import rememberMessageBarState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.onda.home.component.BottomBar
import az.onda.home.component.CustomDrawer
import az.onda.home.domain.BottomBarDestination
import az.onda.home.domain.CustomDrawerState
import az.onda.home.domain.isOpened
import az.onda.home.domain.opposite
import az.onda.shared.Alpha
import az.onda.shared.FontSizes
import az.onda.shared.Resources
import az.onda.shared.RobotoCondensedFont
import az.onda.shared.Surface
import az.onda.shared.SurfaceLighter
import az.onda.shared.TextPrimary
import az.onda.shared.navigation.Screen
import az.onda.shared.util.getScreenWidth
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToAuth: () -> Unit
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    // val currentDestination by remember { mutableSetOf(BottomBarDestination.Home) }
    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()
            when {
                route.contains(BottomBarDestination.Home.screen.toString()) -> BottomBarDestination.Home
                route.contains(BottomBarDestination.Search.screen.toString()) -> BottomBarDestination.Search
                route.contains(BottomBarDestination.Reservation.screen.toString()) -> BottomBarDestination.Reservation
                route.contains(BottomBarDestination.Profile.screen.toString()) -> BottomBarDestination.Profile
                else -> BottomBarDestination.Home
            }
        }
    }
    val screenWidth = remember { getScreenWidth() }
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }

    val offsetValue by remember {
        derivedStateOf {
            (screenWidth / 1.5).dp
        }
    }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp
    )

    val animatedBackground by animateColorAsState(
        targetValue = if (drawerState.isOpened()) SurfaceLighter else Surface
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )

    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 20.dp else 0.dp
    )

    val viewModel = koinViewModel<HomeGraphViewModel>()
    val messageBarState = rememberMessageBarState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
            .systemBarsPadding()
    ) {
        CustomDrawer(
            onProfileClick = { /*TODO*/ },
            onReservationsClick = { /*TODO*/ },
            onNotificationsClick = { /*TODO*/ },
            onContactsClick = { /*TODO*/ },
            onSignOutClick = {
                viewModel.signOut(
                    onSuccess = navigateToAuth,
                    onError = { message ->
                        // Handle error if needed
                        messageBarState.addError(message)
                    }
                )
            },
            onAdminPanelClick = { /*TODO*/ }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(animatedRadius))
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(animatedRadius),
                    ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                    spotColor = Color.Black.copy(alpha = Alpha.DISABLED),
                )
        ) {
            Scaffold (
                containerColor = Surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            AnimatedContent(
                                targetState = selectedDestination
                            ) { destination ->
                                Text(
                                    text = destination.title,
                                    fontFamily = RobotoCondensedFont(),
                                    fontSize = FontSizes.LARGE,
                                    color = TextPrimary
                                )
                            }
                        },
                        navigationIcon = {
                            AnimatedContent(
                                targetState = drawerState
                            ) { state ->
                                IconButton(onClick = {
                                    drawerState = state.opposite()
                                }) {
                                    Icon(
                                        painter = painterResource(
                                            if (state.isOpened())
                                                Resources.Icon.Close
                                            else
                                                Resources.Icon.MenuList
                                        ),
                                        contentDescription = if (state.isOpened()) "Menu icon" else "Close icon"
                                    )
                                }

                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Surface,
                            scrolledContainerColor = Surface,
                            navigationIconContentColor = TextPrimary,
                            titleContentColor = TextPrimary,
                            actionIconContentColor = TextPrimary,
                        )
                    )
                }
            ) { padding ->
                ContentWithMessageBar(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                    contentBackgroundColor = Surface
                ) {
                    // HomeGraph content goes here
                    Column (
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        NavHost(
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            startDestination = Screen.HomeGraph,
                        ) {
                            composable<Screen.HomeGraph>{ }
                            composable<Screen.Reservation> { }
                            composable<Screen.Categories>{ }
                            composable<Screen.Profile>{ }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            BottomBar(
                                selected = selectedDestination,
                                onSelect = { destination ->
                                    navController.navigate(destination.screen) {
                                        launchSingleTop = true
                                        popUpTo<Screen.HomeGraph> {
                                            saveState = true
                                            inclusive = false
                                        }
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
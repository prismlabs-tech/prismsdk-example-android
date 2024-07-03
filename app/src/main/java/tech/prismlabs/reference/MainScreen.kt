package tech.prismlabs.reference

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.prismlabs.reference.settings.SettingsService
import tech.prismlabs.reference.ui.NavigationItem
import tech.prismlabs.reference.ui.onboarding.onboardingGraph
import tech.prismlabs.reference.ui.scans.scansGraph

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val currentContext = LocalContext.current
    val navController: NavHostController = rememberNavController()
    val settings: SettingsService = remember { SettingsService(currentContext) }
    val didOnboard by settings.onboardingComplete.collectAsStateWithLifecycle(false)

    LaunchedEffect(didOnboard) {
        settings.checkOnboardingComplete()
        if (!didOnboard) {
            navController.navigate(NavigationItem.Onboarding.UserInfo.route) {
                popUpTo(NavigationItem.Splash.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(NavigationItem.ScanList.route) {
                popUpTo(NavigationItem.Splash.route) {
                    inclusive = true
                }
            }
        }
    }

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = NavigationItem.Splash.route
    ) {
        composable(NavigationItem.Splash.route) {
            SplashScreen()
        }
        scansGraph(navController = navController)
        onboardingGraph(navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}


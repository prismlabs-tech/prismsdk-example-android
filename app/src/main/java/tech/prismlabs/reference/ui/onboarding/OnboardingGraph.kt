package tech.prismlabs.reference.ui.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.prismlabs.reference.ui.NavigationItem
import tech.prismlabs.reference.ui.onboarding.prepare.PrepareScreen
import tech.prismlabs.reference.ui.onboarding.privacy.PrivacyPolicyScreen
import tech.prismlabs.reference.ui.onboarding.userinfo.UserInfoScreen

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    navigation(startDestination = NavigationItem.Onboarding.UserInfo.route, route = "onboarding") {
        composable(NavigationItem.Onboarding.UserInfo.route) {
            UserInfoScreen(navController = navController)
        }
        composable(NavigationItem.Onboarding.Privacy.route) {
            PrivacyPolicyScreen(navController = navController)
        }
        composable(NavigationItem.Onboarding.Prepare.route) {
            PrepareScreen(navController = navController)
        }
    }
}
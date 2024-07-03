package tech.prismlabs.reference.ui.scans

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.prismlabs.reference.ui.NavigationItem
import tech.prismlabs.reference.ui.scans.details.data.ScanDetailsViewModel
import tech.prismlabs.reference.ui.scans.details.views.ScanDetailsScreen
import tech.prismlabs.reference.ui.scans.list.ScanListScreen

fun NavGraphBuilder.scansGraph(navController: NavController) {

    navigation(startDestination = NavigationItem.Onboarding.UserInfo.route, route = "scans") {
        composable(NavigationItem.ScanList.route) {
            ScanListScreen(navController = navController)
        }
        composable("${NavigationItem.ScanDetails.route}/{id}") {
            val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
            }
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val id = it.arguments?.getString("id")
                val viewModel: ScanDetailsViewModel = viewModel()
                if (id != null) {
                    viewModel.fetchDetails(id)
                }
                ScanDetailsScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}
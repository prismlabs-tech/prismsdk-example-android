package tech.prismlabs.reference.ui.onboarding.userinfo

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tech.prismlabs.reference.ui.NavigationItem

@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val context = LocalContext.current
    val viewModel: UserInfoViewModel = viewModel(factory = UserInfoViewModelFactory(context))
    UserInfoForm(
        modifier = modifier
            .padding(16.dp),
        viewModel = viewModel,
        onContinue = {
            navController.navigate(NavigationItem.Onboarding.Privacy.route)
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserInfoScreenPreview() {
    val nav = rememberNavController()
    UserInfoScreen(navController = nav)
}
package tech.prismlabs.reference.ui.onboarding.privacy

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
fun PrivacyPolicyScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: PrivacyPolicyViewModel = viewModel(factory = PrivacyPolicyViewModelFactory(context))
    PrivacyPolicyForm(
        modifier = modifier.padding(16.dp),
        viewModel = viewModel,
        onContinue = {
            navController.navigate(NavigationItem.Onboarding.Prepare.route)
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PrivacyPolicyScreenPreview() {
    val nav = rememberNavController()
    PrivacyPolicyScreen(navController = nav)
}

package tech.prismlabs.reference.ui.onboarding.prepare

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tech.prismlabs.reference.R
import tech.prismlabs.reference.ui.NavigationItem
import tech.prismlabs.reference.ui.components.PrimaryButton
import tech.prismlabs.reference.ui.theme.PrismColors

@Composable
fun PrepareScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.onboarding_prepare_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.onboarding_prepare_subtitle),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(15.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            border = BorderStroke(1.dp, PrismColors.prismBase20),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                PrepareStep(
                    color = PrismColors.prismPurple,
                    icon = painterResource(tech.prismlabs.prismsdk.R.drawable.prism_pose),
                    title = stringResource(R.string.onboarding_prepare_step_1_title),
                    subtitle = stringResource(R.string.onboarding_prepare_step_1_subtitle),
                )
                Spacer(modifier = Modifier.height(15.dp))

                PrepareStep(
                    color = PrismColors.prismBlue,
                    icon = painterResource(tech.prismlabs.prismsdk.R.drawable.prism_ruler),
                    title = stringResource(R.string.onboarding_prepare_step_2_title),
                    subtitle = stringResource(R.string.onboarding_prepare_step_2_subtitle),
                )
                Spacer(modifier = Modifier.height(15.dp))

                PrepareStep(
                    color = PrismColors.prismMint,
                    icon = painterResource(R.drawable.level_phone_icon),
                    title = stringResource(R.string.onboarding_prepare_step_3_title),
                    subtitle = stringResource(R.string.onboarding_prepare_step_3_subtitle),
                )
                Spacer(modifier = Modifier.height(15.dp))

                PrepareStep(
                    color = PrismColors.prismGreen,
                    icon = painterResource(tech.prismlabs.prismsdk.R.drawable.prism_body_scan),
                    title = stringResource(R.string.onboarding_prepare_step_4_title),
                    subtitle = stringResource(R.string.onboarding_prepare_step_4_subtitle),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.button_continue),
            onClick = {
                navController.navigate(NavigationItem.ScanList.route) {
                    popUpTo(NavigationItem.Splash.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrepareScreenPreview() {
    val nav = rememberNavController()
    PrepareScreen(
        modifier = Modifier.padding(16.dp),
        navController = nav
    )
}
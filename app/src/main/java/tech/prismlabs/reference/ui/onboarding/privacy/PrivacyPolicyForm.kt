package tech.prismlabs.reference.ui.onboarding.privacy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import tech.prismlabs.reference.R
import tech.prismlabs.reference.ui.components.PrimaryButton

@Composable
fun PrivacyPolicyForm(
    modifier: Modifier = Modifier,
    viewModel: PrivacyPolicyViewModel,
    onContinue: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val isValid by viewModel.isValid.collectAsStateWithLifecycle()
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.onboarding_privacy_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.onboarding_privacy_description),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(15.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            uriHandler.openUri(
                                "https://www.prismlabs.tech/privacy"
                            )
                        }
                    )
            ) {
                Text(
                    text = stringResource(R.string.onboarding_privacy_view_terms_button_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_privacy_view_terms_button_learn_more),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowForward,
                        modifier = Modifier.height(20.dp).padding(start = 2.dp),
                        contentDescription = ""
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = viewModel.agreeToPrivacyPolicy,
                onCheckedChange = { viewModel.updateAgreeToPrivacyPolicy(it) }
            )
            Text(
                text = stringResource(R.string.onboarding_privacy_agree_to_toc_checkbox),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = viewModel.agreeToSharingWithPrism,
                onCheckedChange = { viewModel.updateAgreeToSharingWithPrism(it) }
            )
            Text(
                text = stringResource(R.string.onboarding_privacy_agree_to_data_sharing_checkbox),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.button_continue),
            enabled = isValid,
            onClick = {
                // TODO: Handle error cases here. Display a banner potentially.
                scope.launch {
                    isSaving = true
                    viewModel.save()
                    isSaving = false
                    onContinue()
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrivacyPolicyFormPreview() {
    val context = LocalContext.current
    PrivacyPolicyForm(
        modifier = Modifier
            .padding(16.dp),
        viewModel = viewModel(factory = PrivacyPolicyViewModelFactory(context)),
    )
}

package tech.prismlabs.reference.ui.onboarding.userinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import tech.prismlabs.reference.R
import tech.prismlabs.reference.ui.components.PrimaryButton
import tech.prismlabs.reference.ui.components.SegmentedControl

@Composable
fun UserInfoForm(
    modifier: Modifier = Modifier,
    viewModel: UserInfoViewModel,
    onContinue: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isValid by viewModel.isValid.collectAsStateWithLifecycle()
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.onboarding_user_info_get_started),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = stringResource(R.string.onboarding_user_info_get_started_prompt),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            modifier = Modifier.padding(bottom = 1.dp),
            text = stringResource(R.string.onboarding_user_info_email_title),
            fontWeight = FontWeight.Bold,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onAny = { keyboardController?.hide()}
            ),
            label = { Text(stringResource(R.string.onboarding_user_info_email_placeholder)) },
            value = viewModel.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = stringResource(R.string.onboarding_user_info_sex_title),
            fontWeight = FontWeight.Bold,
        )
        val genders = listOf(
            stringResource(R.string.onboarding_user_info_sex_male),
            stringResource(R.string.onboarding_user_info_sex_female),
            stringResource(R.string.onboarding_user_info_sex_non_binary)
        )
        SegmentedControl(
            modifier = Modifier.fillMaxWidth(),
            items = genders,
            selectedIndex = viewModel.sex
        ) {
            viewModel.updateSex(it)
        }

        FormSpacer()

        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = stringResource(R.string.onboarding_user_info_height_title),
            fontWeight = FontWeight.Bold,
        )
        HeightPickerField(
            modifier = Modifier,
            currentValue = viewModel.height,
            onChange = {
                viewModel.updateHeight(it)
            }
        )

        FormSpacer()

        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = stringResource(R.string.onboarding_user_info_weight_title),
            fontWeight = FontWeight.Bold,
        )
        WeightPickerField(
            modifier = Modifier,
            currentValue = viewModel.weight,
            onChange = {
                viewModel.updateWeight(it)
            }
        )

        FormSpacer()

        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = stringResource(R.string.onboarding_user_info_age_title),
            fontWeight = FontWeight.Bold,
        )
        AgePickerField(
            modifier = Modifier,
            currentValue = viewModel.age,
            onChange = {
                viewModel.updateAge(it)
            }
        )

        FormSpacer()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.onboarding_user_info_3d_avatar_title),
                fontWeight = FontWeight.Bold,
            )
            Switch(
                checked = viewModel.show3dModel,
                onCheckedChange = {
                    viewModel.updateShow3dModel(it)
                },
                thumbContent = if (viewModel.show3dModel) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }

        FormSpacer()

        PrimaryButton(
            text = stringResource(R.string.button_continue),
            active = isSaving,
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

@Composable
fun FormSpacer() {
    Spacer(modifier = Modifier.size(20.dp))
}

@Preview(showSystemUi = true)
@Composable
fun UserInfoFormPreview() {
    val context = LocalContext.current
    UserInfoForm(
        modifier = Modifier
            .padding(16.dp),
        viewModel = viewModel(factory = UserInfoViewModelFactory(context)),
    )
}

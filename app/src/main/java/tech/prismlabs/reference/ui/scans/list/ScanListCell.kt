package tech.prismlabs.reference.ui.scans.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tech.prismlabs.prismsdk.api_client.BodyFat
import tech.prismlabs.prismsdk.api_client.Height
import tech.prismlabs.prismsdk.api_client.HeightUnit
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.prismsdk.api_client.ScanStatus
import tech.prismlabs.prismsdk.api_client.UserSex
import tech.prismlabs.prismsdk.api_client.Weight
import tech.prismlabs.prismsdk.api_client.WeightUnit
import tech.prismlabs.reference.R
import tech.prismlabs.reference.extensions.ScanCreatedAt
import tech.prismlabs.reference.extensions.formatted
import tech.prismlabs.reference.extensions.formattedBodyFatPercentage
import tech.prismlabs.reference.extensions.formattedLeanMassPercentage
import tech.prismlabs.reference.extensions.localCreatedAt
import tech.prismlabs.reference.settings.SettingsService
import tech.prismlabs.reference.settings.userSex
import tech.prismlabs.reference.ui.theme.PrismColors
import java.time.ZonedDateTime

@Composable
fun ScanListCell(
    modifier: Modifier = Modifier,
    scan: Scan,
    onClick: () -> Unit = {}
) {
    if (scan.status == ScanStatus.READY) {
        ProcessedScanContent(
            modifier = modifier,
            scan = scan,
            onClick = onClick
        )
    } else {
        ProcessingScanContent(
            modifier = modifier,
            scan = scan,
            onClick = onClick
        )
    }
}

@Composable
private fun ProcessingScanContent(
    modifier: Modifier = Modifier,
    scan: Scan,
    onClick: () -> Unit = {}
) {
    val stateName = when(scan.status) {
        ScanStatus.CREATED -> stringResource(R.string.scan_status_created)
        ScanStatus.PROCESSING -> stringResource(R.string.scan_status_processing)
        ScanStatus.READY -> stringResource(R.string.scan_status_ready)
        ScanStatus.FAILED -> stringResource(R.string.scan_status_failed)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                4.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                enabled = scan.status == ScanStatus.READY,
            ) {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 8.dp, start = 8.dp)
                    .background(
                        PrismColors.prismBase5,
                        shape = CircleShape
                    )
                    .size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(35.dp),
                    painter = painterResource(tech.prismlabs.prismsdk.R.drawable.prism_body_scan),
                    tint = PrismColors.prismBase50,
                    contentDescription = "Icon"
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = scan.localCreatedAt,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stateName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
}

@Composable
private fun ProcessedScanContent(
    modifier: Modifier = Modifier,
    scan: Scan,
    onClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val settings = SettingsService(context)
    var sex by remember { mutableStateOf(UserSex.MALE) }

    LaunchedEffect(sex) {
        coroutineScope.launch {
            val userData = settings.getUserData()
            sex = userData.userSex
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                4.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 8.dp, start = 8.dp)
                    .size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp),
                    painter = if (sex == UserSex.FEMALE) {
                        painterResource(R.drawable.female_avatar)
                    } else {
                        painterResource(R.drawable.male_avatar)
                    },
                    contentDescription = "User Sex icon"
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = scan.createdAt.format(ScanCreatedAt),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = scan.weight.formatted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${scan.bodyFat?.formattedBodyFatPercentage} fat, ${scan.bodyFat?.formattedLeanMassPercentage} lean",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 24.dp, bottom = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.scan_view_full_report_button),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Icon(
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp)
                    .size(18.dp),
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Select Button"
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun ScanListCellPreview() {
    Column {
        ScanListCell(
            scan = Scan(
                id = "",
                userId = "",
                userToken = "",
                status = ScanStatus.PROCESSING,
                deviceConfigName = "",
                weight = Weight(value = 67.5, unit = WeightUnit.KILOGRAMS),
                height = Height(value = 0.0, unit = HeightUnit.INCHES),
                measurements = null,
                bodyFat = null,
                scanAssets = null,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                assetConfigId = null,
            )
        )

        ScanListCell(
            scan = Scan(
                id = "",
                userId = "",
                userToken = "",
                status = ScanStatus.READY,
                deviceConfigName = "",
                weight = Weight(value = 67.5, unit = WeightUnit.KILOGRAMS),
                height = Height(value = 0.0, unit = HeightUnit.INCHES),
                measurements = null,
                bodyFat = BodyFat(
                    bodyFatPercentage = 24.0,
                    leanMassPercentage = 76.5,
                    fatMass = null,
                    leanMass = null
                ),
                scanAssets = null,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                assetConfigId = null,
            )
        )
    }
}
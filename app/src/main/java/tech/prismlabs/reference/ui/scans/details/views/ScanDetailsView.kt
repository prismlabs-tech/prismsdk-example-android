package tech.prismlabs.reference.ui.scans.details.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.prismlabs.prismsdk.api_client.Height
import tech.prismlabs.prismsdk.api_client.HeightUnit
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.prismsdk.api_client.ScanStatus
import tech.prismlabs.prismsdk.api_client.Weight
import tech.prismlabs.prismsdk.api_client.WeightUnit
import java.io.File
import java.time.ZonedDateTime

@Composable
fun ScanDetailsView(
    modifier: Modifier = Modifier,
    scan: Scan,
    avatar: File?
) {
    if (scan.status == ScanStatus.READY) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            AvatarView(modifier = modifier.weight(2f), file = avatar)
            HorizontalDivider()
            StatsView(modifier = Modifier.weight(1f), scan = scan)
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Scan is still processing. Check back later.",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScanDetailsViewPreview() {
    ScanDetailsView(
        scan = Scan(
            id = "preview",
            userId = "",
            userToken = "",
            status = ScanStatus.READY,
            deviceConfigName = "",
            weight = Weight(value = 67.5, unit = WeightUnit.KILOGRAMS),
            height = Height(value = 0.0, unit = HeightUnit.INCHES),
            measurements = null,
            bodyFat = null,
            scanAssets = null,
            createdAt = ZonedDateTime.now(),
            updatedAt = ZonedDateTime.now(),
            assetConfigId = null,
        ),
        avatar = null
    )
}

@Preview(showSystemUi = true)
@Composable
fun ScanDetailsViewProcessingPreview() {
    ScanDetailsView(
        scan = Scan(
            id = "preview",
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
        ),
        avatar = null
    )
}
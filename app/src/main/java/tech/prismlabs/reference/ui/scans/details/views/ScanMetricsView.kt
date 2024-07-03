package tech.prismlabs.reference.ui.scans.details.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.prismlabs.prismsdk.api_client.BodyFat
import tech.prismlabs.prismsdk.api_client.Height
import tech.prismlabs.prismsdk.api_client.HeightUnit
import tech.prismlabs.prismsdk.api_client.Measurements
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.prismsdk.api_client.ScanStatus
import tech.prismlabs.prismsdk.api_client.Weight
import tech.prismlabs.prismsdk.api_client.WeightUnit
import tech.prismlabs.reference.ui.scans.details.data.ScanDetailItem
import tech.prismlabs.reference.ui.theme.PrismColors
import java.time.ZonedDateTime

@Composable
fun ScanMetricsView(
    modifier: Modifier = Modifier,
    scan: Scan,
    state: LazyListState = rememberLazyListState()
) {
    LazyRow(
        modifier = modifier,
        state = state
    ) {
        items(ScanDetailItem.fromScan(scan)) { item ->
            ScanDetailItemView(item = item)
        }
    }
}

@Composable
fun ScanDetailItemView(
    item: ScanDetailItem,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .size(150.dp, 100.dp)
            .padding(8.dp),
        border = BorderStroke(1.dp, PrismColors.prismBase10),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = item.type.title(context),
                style = MaterialTheme.typography.labelLarge
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = item.formattedValue,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp, bottom = 5.dp),
                    text = item.formattedValueSuffix,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanMetricsViewPreview() {
    ScanMetricsView(
        scan = Scan(
            id = "preview",
            userId = "",
            userToken = "",
            status = ScanStatus.PROCESSING,
            deviceConfigName = "",
            weight = Weight(value = 67.5, unit = WeightUnit.KILOGRAMS),
            height = Height(value = 0.0, unit = HeightUnit.INCHES),
            measurements = Measurements(
                neckFit = 1.0,
                shoulderFit = 1.0,
                upperChestFit = 1.0,
                chestFit = 1.0,
                lowerChestFit = 1.0,
                waistFit = 1.0,
                waistNavyFit = 1.0,
                stomachFit = 1.0,
                hipsFit = 1.0,
                upperThighLeftFit = 1.0,
                upperThighRightFit = 1.0,
                lowerThighLeftFit = 1.0,
                lowerThighRightFit = 1.0,
                thighLeftFit = 1.0,
                thighRightFit = 1.0,
                calfLeftFit = 1.0,
                calfRightFit = 1.0,
                ankleLeftFit = 1.0,
                ankleRightFit = 1.0,
                midArmLeftFit = 1.0,
                midArmRightFit = 1.0,
                lowerArmLeftFit = 1.0,
                lowerArmRightFit = 1.0,
                waistToHipRatio = 1.0
            ),
            bodyFat = BodyFat(
                bodyFatPercentage = 40.0,
                leanMassPercentage = 22.0,
                fatMass = 4.0,
                leanMass = 2.0,
            ),
            scanAssets = null,
            createdAt = ZonedDateTime.now(),
            updatedAt = ZonedDateTime.now(),
            assetConfigId = null,
        ),
    )
}
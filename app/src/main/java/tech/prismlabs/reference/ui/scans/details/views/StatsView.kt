package tech.prismlabs.reference.ui.scans.details.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import tech.prismlabs.prismsdk.api_client.BodyFat
import tech.prismlabs.prismsdk.api_client.Height
import tech.prismlabs.prismsdk.api_client.HeightUnit
import tech.prismlabs.prismsdk.api_client.Measurements
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.prismsdk.api_client.ScanStatus
import tech.prismlabs.prismsdk.api_client.Weight
import tech.prismlabs.prismsdk.api_client.WeightUnit
import tech.prismlabs.reference.ui.scans.details.data.Category
import java.time.ZonedDateTime

@Composable
fun StatsView(
    modifier: Modifier = Modifier,
    scan: Scan,
) {
    val context = LocalContext.current
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        CategoryPicker(
            tabs = Category.titles(context),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = {
                selectedTabIndex = it
                coroutineScope.launch {
                    when (Category.fromIndex(it)) {
                        Category.KEY_STATS -> coroutineScope.launch {
                            state.animateScrollToItem(index = 0)
                        }
                        Category.UPPER_TORSO -> coroutineScope.launch {
                            state.animateScrollToItem(index = 6)
                        }
                        Category.LOWER_TORSO -> coroutineScope.launch {
                            state.animateScrollToItem(index = 10)
                        }
                        Category.ARMS -> coroutineScope.launch {
                            state.animateScrollToItem(index = 12)
                        }
                        Category.LEGS -> coroutineScope.launch {
                            state.animateScrollToItem(index = 14)
                        }
                    }
                }
            }
        )
        ScanMetricsView(
            state = state,
            scan = scan
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatsViewPreview() {
    StatsView(
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

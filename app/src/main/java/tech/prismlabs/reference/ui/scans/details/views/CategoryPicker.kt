package tech.prismlabs.reference.ui.scans.details.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.prismlabs.reference.ui.scans.details.data.Category

@Composable
fun CategoryPicker(
    tabs: List<String> = emptyList(),
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    ScrollableTabRow(
        contentColor = MaterialTheme.colorScheme.secondary,
        tabs = {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.padding(16.dp),
                    selected = index == selectedTabIndex,
                    onClick = { onTabSelected(index) },
                ) {
                    Text(
                        text = title
                    )
                }
            }
        },
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    currentTabPosition = tabPositions[selectedTabIndex],
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryPickerPreview() {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    CategoryPicker(
        tabs = Category.titles(context),
        selectedTabIndex = selectedTab,
        onTabSelected = { selectedTab = it }
    )
}

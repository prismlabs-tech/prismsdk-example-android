package tech.prismlabs.reference.ui.onboarding.userinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import tech.prismlabs.reference.ui.components.PickerField

@Composable
fun WeightPickerField(
    modifier: Modifier = Modifier,
    currentValue: Int,
    onChange: (Int) -> Unit
) {
    val range: IntRange = 10..600
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        DropdownMenu(
            modifier = Modifier
                .requiredSizeIn(maxHeight = 200.dp),
            offset = DpOffset(16.dp, 0.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            range.forEach { pound ->
                DropdownMenuItem(
                    text = { Text("$pound lbs") },
                    onClick = {
                        onChange(pound)
                        expanded = false
                    }
                )
            }
        }
        PickerField(
            modifier = modifier,
            label = "$currentValue lbs",
            onClick = { expanded = !expanded },
            onSubtractClick = {
                onChange(currentValue - 1)
            },
            enableSubtractClick = currentValue > range.first,
            onAddClick = {
                onChange(currentValue + 1)
            },
            enableAddClick = currentValue < range.last
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeightPickerFieldPreview() {
    WeightPickerField(currentValue = 150, onChange = {})
}

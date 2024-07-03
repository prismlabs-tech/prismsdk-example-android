package tech.prismlabs.reference.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun PickerField(
    modifier: Modifier = Modifier,
    label: String = "",
    onClick: () -> Unit = {},
    enableContentClick: Boolean = true,
    onSubtractClick: () -> Unit = {},
    enableSubtractClick: Boolean = true,
    onAddClick: () -> Unit = {},
    enableAddClick: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        OutlinedButton(
            modifier = Modifier
                .weight(1.0f)
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            enabled = enableContentClick,
            onClick = onClick
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = label,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedButton(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                1.dp, when (enableSubtractClick) {
                    true -> MaterialTheme.colorScheme.outline
                    false -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                }
            ),
            colors = when (enableSubtractClick) {
                true -> ButtonDefaults.outlinedButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )

                false -> ButtonDefaults.outlinedButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            onClick = onSubtractClick,
            enabled = enableSubtractClick
        ) {
            Text(
                text = "-",
                fontSize = TextUnit(40f, TextUnitType.Sp),
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedButton(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                1.dp, when (enableAddClick) {
                    true -> MaterialTheme.colorScheme.outline
                    false -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                }
            ),
            colors = when (enableAddClick) {
                true -> ButtonDefaults.outlinedButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )

                false -> ButtonDefaults.outlinedButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            onClick = onAddClick,
            enabled = enableAddClick
        ) {
            Text(
                text = "+",
                fontSize = TextUnit(25f, TextUnitType.Sp),
                fontWeight = FontWeight.Light
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PickerFieldPreview() {
    PickerField(label = "Preview")
}

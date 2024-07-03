package tech.prismlabs.reference.ui.onboarding.userinfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import tech.prismlabs.reference.ui.components.PickerField
import tech.prismlabs.reference.ui.components.PrimaryButton

@Composable
fun HeightPickerField(
    modifier: Modifier = Modifier,
    currentValue: Int,
    onChange: (Int) -> Unit
) {
    val range: IntRange = 36..107
    val feetRange: IntRange = 3..8
    val inchesRange: IntRange = 1..11
    var showDialog by remember { mutableStateOf(false) }

    val feet = currentValue / 12
    val inches = currentValue % 12

    PickerField(
        modifier = modifier,
        label = "${feet}ft ${inches}in",
        onClick = {
            showDialog = true
        },
        onSubtractClick = {
            onChange(currentValue - 1)
        },
        enableSubtractClick = currentValue > range.first,
        onAddClick = {
            onChange(currentValue + 1)
        },
        enableAddClick = currentValue < range.last
    )

    if (showDialog) {
        var showFeet by remember { mutableStateOf(false) }
        var showInches by remember { mutableStateOf(false) }
        Dialog(
            onDismissRequest = {
                showDialog = false
            }
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .height(200.dp)
                    .width(350.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        text = "Select your height",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
                        ) {
                            OutlinedButton(
                                modifier = Modifier
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                colors = ButtonDefaults.outlinedButtonColors().copy(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                onClick = { showFeet = !showFeet }
                            ) {
                                Text(
                                    text = "$feet ft",
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                            DropdownMenu(
                                expanded = showFeet,
                                onDismissRequest = { showFeet = false }
                            ) {
                                feetRange.forEach { feet ->
                                    DropdownMenuItem(
                                        text = { Text("$feet ft") },
                                        onClick = {
                                            onChange((feet * 12) + inches)
                                            showFeet = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
                        ) {
                            OutlinedButton(
                                modifier = Modifier
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                colors = ButtonDefaults.outlinedButtonColors().copy(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                onClick = { showInches = !showInches }
                            ) {
                                Text(
                                    text = "$inches in",
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                            DropdownMenu(
                                expanded = showInches,
                                onDismissRequest = { showInches = false }
                            ) {
                                inchesRange.forEach { inch ->
                                    DropdownMenuItem(
                                        text = { Text("$inch in") },
                                        onClick = {
                                            onChange((feet * 12) + inch)
                                            showInches = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    PrimaryButton(
                        modifier = Modifier
                            .padding(16.dp),
                        text = "Continue"
                    ) {
                        showDialog = false
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeightPickerFieldPreview() {
    HeightPickerField(currentValue = 60, onChange = {})
}

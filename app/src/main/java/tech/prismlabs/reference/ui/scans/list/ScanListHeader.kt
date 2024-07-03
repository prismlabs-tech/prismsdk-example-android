package tech.prismlabs.reference.ui.scans.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme

@Composable
fun ScanListHeader(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.title2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScanListHeaderPreview() {
    ScanListHeader(title = "Preview Header")
}

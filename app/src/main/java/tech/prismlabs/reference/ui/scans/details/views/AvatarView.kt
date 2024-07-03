package tech.prismlabs.reference.ui.scans.details.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun AvatarView(
    modifier: Modifier = Modifier,
    file: File? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (file != null) {
            Text(text = "3D Avatar View: ${file.path}")
        } else {
            Text(text = "No Avatar Downloaded")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarViewPreview() {
    AvatarView()
}
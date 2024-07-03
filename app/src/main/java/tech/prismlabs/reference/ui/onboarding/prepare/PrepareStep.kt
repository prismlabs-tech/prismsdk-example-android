package tech.prismlabs.reference.ui.onboarding.prepare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.prismlabs.reference.R
import tech.prismlabs.reference.ui.theme.PrismColors

@Composable
fun PrepareStep(
    modifier: Modifier = Modifier,
    color: Color,
    icon: Painter,
    title: String,
    subtitle: String,
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    color,
                    shape = CircleShape
                )
        ) {
            Icon(
                modifier = Modifier
                    .padding(20.dp)
                    .size(40.dp),
                painter = icon,
                contentDescription = "The icon of the step", tint = Color.White
            )
        }
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun PreviewPrepareStep() {
    PrepareStep(
        modifier = Modifier.padding(16.dp),
        color = PrismColors.prismPurple,
        icon = painterResource(R.drawable.ic_launcher_background),
        title = "Prepare Yourself",
        subtitle = "Wear form-fitting clothes and keep hair off your shoulders and face.",
    )
}
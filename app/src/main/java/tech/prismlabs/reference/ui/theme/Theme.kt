package tech.prismlabs.reference.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import tech.prismlabs.reference.ui.components.PrimaryButton

private val DarkColorScheme = darkColorScheme(
    primary = PrismColors.primaryColor,
    secondary = PrismColors.secondaryColor,
    tertiary = PrismColors.prismBlue,
    error = PrismColors.prismRed,
    onSurface = PrismColors.prismWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PrismColors.primaryColor,
    secondary = PrismColors.secondaryColor,
    tertiary = PrismColors.prismBlue,
    error = PrismColors.prismRed,
    onSurface = PrismColors.prismBlack,
    outline = PrismColors.prismBase20

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PrismReferenceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PrismReferenceLightThemePreview() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                PrimaryButton(
                    text = "Enabled",
                    enabled = true,
                    onClick = { }
                )
                PrimaryButton(
                    text = "Disabled",
                    enabled = false,
                    onClick = { }
                )
            }
        }
    )
}

@Preview(backgroundColor = 0xFF121111, showBackground = true)
@Composable
fun PrismReferenceDarkThemePreview() {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                PrimaryButton(
                    text = "Enabled",
                    enabled = true,
                    onClick = { }
                )
                PrimaryButton(
                    text = "Disabled",
                    enabled = false,
                    onClick = { }
                )
            }
        }
    )
}
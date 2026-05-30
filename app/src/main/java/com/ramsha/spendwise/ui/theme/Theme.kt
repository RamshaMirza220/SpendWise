package com.ramsha.spendwise.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryGreen = Color(0xFF1B8A5A)
val PrimaryContainer = Color(0xFFB7F0D4)
val SecondaryBlue = Color(0xFF1565C0)
val BackgroundLight = Color(0xFFF6F8FA)
val SurfaceColor = Color(0xFFFFFFFF)
val ErrorRed = Color(0xFFD32F2F)
val OnPrimary = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    secondary = SecondaryBlue,
    background = BackgroundLight,
    surface = SurfaceColor,
    error = ErrorRed,
)

@Composable
fun SpendWiseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}

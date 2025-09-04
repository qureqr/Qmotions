package org.qure.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = HackerCyan,
    background = DarkBlueBackground,
    surface = DarkSurface,
    onPrimary = Color.Black,          // Текст на основных элементах (кнопках)
    onBackground = HackerCyan,        // Текст на фоне
    onSurface = HackerCyan            // Текст на поверхностях
)

@Composable
fun QmotionsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = HackerTypography,
        content = content
    )
}
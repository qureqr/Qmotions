package org.qure.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = AccentColor,      // Основной акцентный цвет
    background = BackgroundColor,   // Цвет фона
    surface = SurfaceColor,     // Цвет поверхностей

    onPrimary = Color.Black,        // Текст на акцентных элементах
    onBackground = PrimaryTextColor,  // Текст на фоне
    onSurface = PrimaryTextColor,   // Текст на поверхностях
    error = ErrorColor
)

@Composable
fun QmotionsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = HackerTypography,
        content = content
    )
}
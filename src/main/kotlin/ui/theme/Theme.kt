package org.qure.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Собираем палитру из наших новых цветов
private val DarkColorPalette = darkColors(
    primary = VibrantCyan,       // Яркий акцент для кнопок и рамок
    background = DeepNightBlue,    // Наш новый фон
    surface = GraphiteSurface,   // Наша новая поверхность

    onPrimary = DarkSurface,           // Текст на кнопках (темный)
    onBackground = SubtleCyan,         // Основной текст на фоне (приглушенный)
    onSurface = SubtleCyan,          // Текст на поверхностях (приглушенный)
    error = WarningYellow          // Цвет для ошибок
)

// 2. Наш главный Composable-компонент темы (без изменений)
@Composable
fun QmotionsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = HackerTypography,
        content = content
    )
}
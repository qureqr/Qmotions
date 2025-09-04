package org.qure.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font

// 1. Загружаем кастомный шрифт из папки `resources/font`
// Убедись, что путь и имя файла верные
private val PixelFamily = FontFamily(Font("font/PixelifySans-VariableFont_wght.ttf"))

// 2. Создаем набор типографики, где наш шрифт будет использоваться по умолчанию
val HackerTypography = Typography(
    defaultFontFamily = PixelFamily
)
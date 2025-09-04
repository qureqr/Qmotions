package org.qure.ui.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlitchBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val gridSize = 25.dp.toPx()
        for (i in 0 until (size.width / gridSize).toInt()) {
            drawLine(
                color = Color.White,
                start = Offset(i * gridSize, 0f),
                end = Offset(i * gridSize, size.height),
                alpha = 0.05f
            )
        }
        for (i in 0 until (size.height / gridSize).toInt()) {
            drawLine(
                color = Color.White,
                start = Offset(0f, i * gridSize),
                end = Offset(size.width, i * gridSize),
                alpha = 0.05f
            )
        }
    }
}
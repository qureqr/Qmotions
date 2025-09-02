package org.qure

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.qure.ui.EmotionVisualizerScreen

fun main() = application {
    val windowState = rememberWindowState()
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Emotion Visualizer",
        undecorated = true,
        transparent = false
    ){
        EmotionVisualizerScreen(
            windowState = windowState,
            onCloseRequest = ::exitApplication,
        )
    }
}
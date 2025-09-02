package org.qure

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.qure.ui.EmotionVisualizerScreen

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Emotion Visualizer") {
        EmotionVisualizerScreen()
    }
}
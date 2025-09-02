package org.qure.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import org.qure.data.EmotionRepository
import org.qure.data.Emotions.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class EmotionUiState(
    val userInput: String = "",
    val currentEmotion: Emotion = BasicEmotion.Neutral,
    val errorMessage: String? = null
)

@Composable
fun WindowScope.EmotionVisualizerScreen(
    windowState: WindowState,
    onCloseRequest: () -> Unit,
) {
    var uiState by remember { mutableStateOf(EmotionUiState()) }
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val onVisualizeClick = {
        val foundEmotion = EmotionRepository.findEmotion(uiState.userInput)
        uiState = if (foundEmotion != null) {
            uiState.copy(currentEmotion = foundEmotion, errorMessage = null)
        } else {
            uiState.copy(errorMessage = "Эмоция '${uiState.userInput}' не найдена")
        }
    }
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            WindowDraggableArea {
                TopAppBar(
                    title = { Text("Emotion Visualizer", color = Color.White) },
                    backgroundColor = Color(0xFF212121),
                    elevation = 0.dp,
                    actions = {
                        IconButton(onClick = { windowState.isMinimized = true }) {
                            Icon(Icons.Default.Remove, contentDescription = "Minimize", tint = Color.White)
                        }
                        IconButton(onClick = onCloseRequest) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                )
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = uiState.userInput,
                        onValueChange = { newText -> uiState = uiState.copy(userInput = newText) },
                        label = { Text("Введите эмоцию...") },
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onVisualizeClick) {
                        Text("Визуализировать")
                    }
                }
                uiState.errorMessage?.let {
                    Text(it, color = MaterialTheme.colors.error)
                }
                Canvas(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    drawEmotion(uiState.currentEmotion, pulse)
                }
            }
        }
    }
}
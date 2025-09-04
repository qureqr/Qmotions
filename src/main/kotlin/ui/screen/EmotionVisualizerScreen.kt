package org.qure.ui.screen

import androidx.compose.runtime.withFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import org.qure.ui.shared.randomNormalizedPoint3D
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import org.qure.data.Emotion
import org.qure.data.EmotionRepository
import org.qure.ui.renderEmotion
import org.qure.ui.shared.*
import org.qure.ui.theme.QmotionsTheme
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WindowScope.EmotionVisualizerScreen(
    windowState: WindowState,
    onCloseRequest: () -> Unit,
) {
    var time by remember { mutableStateOf(0f) }
    var uiState by remember { mutableStateOf(EmotionUiState()) }
    var rays by remember { mutableStateOf<List<Ray>>(emptyList()) }

    LaunchedEffect(Unit) {
        var frameCount = 0
        while (true) {
            withFrameNanos { newTime ->
                time = newTime / 1_000_000_000f
                frameCount++
                if (frameCount % 2 == 0) {
                    val newRay = Ray(
                        direction = randomNormalizedPoint3D(),
                        startTime = time,
                        duration = 1.2f
                    )
                    val updatedRays = rays.filter { ray -> time - ray.startTime < ray.duration }
                    rays = updatedRays + newRay
                }
            }
        }
    }

    val points = remember {
        val numPoints = 150; val random = Random(0)
        List(numPoints) {
            val theta = random.nextFloat() * 2 * Math.PI.toFloat()
            val phi = acos(2 * random.nextFloat() - 1)
            Point3D(sin(phi)*cos(theta), sin(phi)*sin(theta), cos(phi), random.nextFloat()*100f)
        }
    }
    val connections = remember {
        val numNeighbors = 3
        points.associateWith { point ->
            points.filter { it != point }.sortedBy { neighbor -> point.distanceTo(neighbor) }.take(numNeighbors)
        }
    }

    val onVisualizeClick = {
        val foundEmotion = EmotionRepository.findEmotion(uiState.userInput)
        uiState = if (foundEmotion != null) {
            uiState.copy(currentEmotion = foundEmotion, errorMessage = null)
        } else {
            uiState.copy(errorMessage = "Эмоция '${uiState.userInput}' не найдена")
        }
    }

    QmotionsTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GlitchBackground()

            Column(modifier = Modifier.fillMaxSize()) {
                WindowDraggableArea {
                    TopAppBar(
                        title = { Text("Emotion Visualizer") },
                        actions = {
                            IconButton(onClick = { windowState.isMinimized = true }) {
                                Icon(Icons.Default.Remove, contentDescription = "Minimize")
                            }
                            IconButton(onClick = onCloseRequest) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
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
                        // --- ИСПРАВЛЕНИЕ ОШИБКИ Type Mismatch ---
                        // Сохраняем эмоцию в переменную и проверяем, что она не null
                        val currentEmotion = uiState.currentEmotion
                        if (currentEmotion != null) {
                            renderEmotion(
                                emotion = currentEmotion, // Передаем non-null эмоцию
                                time = time,
                                points = points,
                                connections = connections,
                                rays = rays
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class EmotionUiState(
    val userInput: String = "",
    val currentEmotion: Emotion? = null, // Эмоция может быть null при старте
    val errorMessage: String? = null
)
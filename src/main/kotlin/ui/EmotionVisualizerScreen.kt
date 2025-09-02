package org.qure.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import org.qure.data.Emotions.*
import org.qure.data.EmotionRepository
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Класс состояния для нашего экрана. Хранит все изменяемые данные.
 */
private data class EmotionUiState(
    val userInput: String = "",
    val currentEmotion: Emotion = BasicEmotion.Neutral,
    val errorMessage: String? = null
)

/**
 * Главный Composable-компонент экрана.
 */
@Composable
fun EmotionVisualizerScreen() {
    var uiState by remember { mutableStateOf(EmotionUiState()) }

    // Настройка бесконечной анимации для пульсации
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Обработчик нажатия на кнопку
    val onVisualizeClick = {
        val foundEmotion = EmotionRepository.findEmotion(uiState.userInput)
        uiState = if (foundEmotion != null) {
            uiState.copy(currentEmotion = foundEmotion, errorMessage = null)
        } else {
            uiState.copy(errorMessage = "Эмоция '${uiState.userInput}' не найдена")
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Верхняя панель с полем ввода и кнопкой
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

            // Отображение сообщения об ошибке
            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error)
            }

            // Холст для рисования эмоций
            Canvas(modifier = Modifier.fillMaxWidth().weight(1f)) {
                drawEmotion(uiState.currentEmotion, pulse)
            }
        }
    }
}

/**
 * Функция-расширение для DrawScope, которая содержит всю логику рисования.
 */
private fun DrawScope.drawEmotion(emotion: Emotion, animationValue: Float) {
    when (emotion) {
        is BasicEmotion.Calmness -> {
            drawLine(
                color = emotion.color,
                start = Offset(x = 0f, y = center.y),
                end = Offset(x = size.width, y = center.y),
                strokeWidth = 8f
            )
        }

        is BasicEmotion.Joy -> {
            // Применяем анимированное масштабирование к центру холста
            withTransform({
                scale(scale = animationValue, pivot = center)
            }) {
                // Рисуем простую звезду (пятиконечник)
                val radius = size.minDimension / 4f
                val path = createStarPath(center, 5, radius, radius / 2)
                drawPath(path, color = emotion.color)
            }
        }

        is BasicEmotion.Sadness -> {
            drawCircle(
                color = emotion.color,
                radius = size.minDimension / 5f,
                center = center.copy(y = center.y + size.height / 4) // Смещен вниз
            )
        }

        is BasicEmotion.Anger -> {
            // Рисуем 20 хаотичных линий, исходящих из центра
            repeat(20) {
                val angle = Random.nextFloat() * 2 * Math.PI
                val length = size.minDimension / 2f * (0.5f + Random.nextFloat() * 0.5f)
                val endX = center.x + length * cos(angle).toFloat()
                val endY = center.y + length * sin(angle).toFloat()
                drawLine(
                    color = emotion.color,
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = (1f + Random.nextFloat() * 4f)
                )
            }
        }

        is ComplexEmotion -> {
            // Для сложных эмоций рисуем полупрозрачные круги для каждого компонента
            emotion.emotions.forEachIndexed { index, subEmotion ->
                drawCircle(
                    color = subEmotion.color,
                    radius = (size.minDimension / 3f) - (index * 40),
                    alpha = 0.6f
                )
            }
        }

        else -> {
            // Заглушка для нейтрального состояния или нереализованных эмоций
            drawCircle(color = emotion.color, radius = 20f, center = center)
        }
    }
}

/**
 * Вспомогательная функция для создания фигуры звезды.
 */
private fun createStarPath(center: Offset, points: Int, outerRadius: Float, innerRadius: Float): androidx.compose.ui.graphics.Path {
    val path = androidx.compose.ui.graphics.Path()
    val angleStep = (2 * Math.PI) / (points * 2)
    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - (Math.PI / 2)
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
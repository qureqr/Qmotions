// отрисовочка
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
import androidx.compose.ui.graphics.drawscope.Stroke         // <-- Важный новый импорт
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

private fun DrawScope.drawEmotion(emotion: Emotion, animationValue: Float) {
    // Используем when для выбора логики отрисовки
    when (emotion) {
        is BasicEmotion.Calmness -> {
            drawLine(
                color = emotion.color,
                start = center.copy(x = 0f),
                end = center.copy(x = size.width),
                strokeWidth = 5f
            )
        }
        is BasicEmotion.Joy -> {
            // Пятиконечник сложнее, его нужно рисовать по точкам
            // Пока просто нарисуем круг для примера
            drawCircle(color = emotion.color, radius = size.minDimension / 4)
        }
        is BasicEmotion.Anger -> {
            // Хаотичные линии из центра
            repeat(10) {
                // Тут понадобится немного математики для случайных линий
            }
        }
        is BasicEmotion.Fear -> {
            val polySides = 6 // Например, шестиугольник
            val baseRadius = size.minDimension / 3f

            // Применяем анимацию сжатия: от 1.0 до 0.8 для BaseEmotion.Fear
            // Если animationValue 1.0 -> 1.1, то инвертируем для сжатия
            val currentScale = 1f - (animationValue - 1f) / 0.1f * emotion.intensity

            withTransform({
                // Масштабируем и поворачиваем многогранник
                // rotation можно сделать анимированным для большей динамики
                scale(scale = currentScale, pivot = center)
                rotate(degrees = 0f, pivot = center) // Можно добавить вращение
            }) {
                // Нарисовать многогранник (только контур)
                drawPath(
                    path = createPolygonPath(center, polySides, baseRadius),
                    color = emotion.color,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                )

                // Хаотичные лучи внутри
                val numRays = 20
                val rayLength = baseRadius * 0.8f // Лучи не достигают края многогранника
                repeat(numRays) {
                    val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
                    val endX = center.x + rayLength * cos(angle)
                    val endY = center.y + rayLength * sin(angle)
                    drawLine(
                        color = emotion.color.copy(alpha = 0.6f), // Полупрозрачные
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 2f + Random.nextFloat() * 2f // Разная толщина
                    )
                }
            }
        }

        is BasicEmotion.Surprise -> {

        }
        is BasicEmotion.Anticipation -> {

        }
        is BasicEmotion.Trust -> {

        }
        is BasicEmotion.Disgust -> {

        }

        is ComplexEmotion -> {
            // Для сложных эмоций можно комбинировать визуализации!
            // Например, нарисовать фон от первой эмоции, а фигуры от второй.
            // Пока просто нарисуем несколько концентрических кругов
            emotion.emotions.forEachIndexed { index, subEmotion ->
                drawCircle(
                    color = subEmotion.color,
                    radius = (size.minDimension / 3) - (index * 30),
                    alpha = 0.5f // Сделаем их полупрозрачными
                )
            }
        }
        // ... другие эмоции
        else -> {
            // Заглушка для нереализованных эмоций
            drawCircle(color = emotion.color, radius = 20f)
        }
    }
}
private fun createPolygonPath(center: Offset, sides: Int, radius: Float): androidx.compose.ui.graphics.Path {
    val path = androidx.compose.ui.graphics.Path()
    val angleStep = (2 * Math.PI / sides).toFloat()
    for (i in 0 until sides) {
        val angle = i * angleStep - (Math.PI / 2).toFloat() // Начинаем сверху
        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
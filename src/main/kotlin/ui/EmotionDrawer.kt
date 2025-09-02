// Файл: org/qure/ui/EmotionDrawer.kt
package org.qure.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import org.qure.data.Emotions.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ИЗМЕНЕНО: Параметр теперь `time`, а не `animationValue` для единообразия.
fun DrawScope.drawEmotion(emotion: Emotion, time: Float) {
    when (emotion) {
        is BasicEmotion.Joy -> {
            // ИСПРАВЛЕНО: Добавлена логика пульсации на основе `time`, как мы делали для Страха.
            val pulseValue = 1.0f + (sin(time * 5f) + 1f) / 2f * 0.1f

            withTransform({ scale(scale = pulseValue, pivot = center) }) {
                drawPath(createStarPath(center, 5, size.minDimension / 4f, size.minDimension / 8f), color = emotion.color)
            }
        }
        is BasicEmotion.Sadness -> {
            // РАБОТАЕТ: Этот код уже правильно использует `time`.
            val baseRadius = size.minDimension / 3f
            val numLines = 20

            for (i in 0..numLines) {
                val path = Path()
                val lat = mapValue(i.toFloat(), 0f, numLines.toFloat(), -90f, 90f)
                val r = baseRadius * cos(Math.toRadians(lat.toDouble())).toFloat()
                val y = center.y + baseRadius * sin(Math.toRadians(lat.toDouble())).toFloat()

                if (r < 1f) continue

                for (angleDeg in 0..360 step 5) {
                    val angleRad = Math.toRadians(angleDeg.toDouble()).toFloat()
                    val distortion = sin(time * 2f + angleRad * 5f) * 10f
                    val distortedRadius = r + distortion
                    val x = center.x + distortedRadius * cos(angleRad)

                    if (angleDeg == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
                path.close()

                drawPath(
                    path,
                    color = emotion.color,
                    style = Stroke(width = 1.5f),
                    alpha = 0.5f
                )
            }
        }

        is BasicEmotion.Anger -> {
            repeat(20) {
                val angle = Random.nextFloat() * 2 * Math.PI
                val length = size.minDimension / 2f * (0.5f + Random.nextFloat() * 0.5f)
                val endX = center.x + length * cos(angle).toFloat()
                val endY = center.y + length * sin(angle).toFloat()
                drawLine(color = emotion.color, start = center, end = Offset(endX, endY), strokeWidth = (1f + Random.nextFloat() * 4f))
            }
        }
        is BasicEmotion.Fear -> {
            // РАБОТАЕТ: Этот код уже правильно использует `time`.
            val compressionValue = 1.0f - (sin(time * 5f) + 1f) / 2f * 0.1f
            val polySides = 6
            val baseRadius = size.minDimension / 3f

            withTransform({ scale(scale = compressionValue, pivot = center) }) {
                drawPath(createPolygonPath(center, polySides, baseRadius), color = emotion.color, style = Stroke(width = 5f))
                repeat(20) {
                    val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
                    val rayLength = baseRadius * 0.8f
                    val endX = center.x + rayLength * cos(angle)
                    val endY = center.y + rayLength * sin(angle)
                    drawLine(color = emotion.color.copy(alpha = 0.6f), start = center, end = Offset(endX, endY), strokeWidth = 2f + Random.nextFloat() * 2f)
                }
            }
        }
        is BasicEmotion.Calmness -> {
            drawLine(color = emotion.color, start = Offset(x = 0f, y = center.y), end = Offset(x = size.width, y = center.y), strokeWidth = 8f)
        }
        is BasicEmotion.Trust, is BasicEmotion.Disgust, is BasicEmotion.Surprise, is BasicEmotion.Anticipation, is BasicEmotion.Neutral -> {
            drawCircle(color = emotion.color, radius = 20f, center = center)
        }
        is ComplexEmotion -> {
            emotion.emotions.forEachIndexed { index, subEmotion ->
                drawCircle(color = subEmotion.color, radius = (size.minDimension / 3) - (index * 40), alpha = 0.6f)
            }
        }
    }
}

// --- Вспомогательные функции (без изменений) ---

private fun createStarPath(center: Offset, points: Int, outerRadius: Float, innerRadius: Float): Path {
    val path = Path()
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

private fun createPolygonPath(center: Offset, sides: Int, radius: Float): Path {
    val path = Path()
    val angleStep = (2 * Math.PI / sides).toFloat()
    for (i in 0 until sides) {
        val angle = i * angleStep - (Math.PI / 2).toFloat()
        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun mapValue(value: Float, fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float {
    return (value - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin
}
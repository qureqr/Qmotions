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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun DrawScope.drawEmotion(
    emotion: Emotion,
    time: Float,
    points: List<Point3D>,
    connections: Map<Point3D, List<Point3D>>
) {
    when (emotion) {
        is BasicEmotion.Fear -> {
            val polySides = 8
            val baseRadius = size.minDimension / 2.5f
            val compressionValue = 1.0f - (sin(time * 4f) + 1f) / 2f * 0.05f

            // 1. Получаем анимированные координаты вершин
            val vertices = getPolygonVertices(center, polySides, baseRadius * compressionValue)

            // 2. Рисуем внутреннюю "паутину"
            for (i in vertices.indices) {
                for (j in i + 1 until vertices.size) {
                    // Эффект свечения для внутренних линий
                    drawLine(color = emotion.color, start = vertices[i], end = vertices[j], strokeWidth = 3f, alpha = 0.2f, cap = StrokeCap.Round) // ИСПРАВЛЕНО: strokeCap -> cap
                    drawLine(color = emotion.color, start = vertices[i], end = vertices[j], strokeWidth = 1f, alpha = 0.5f)
                }
            }

            // 3. Рисуем основную рамку многогранника
            val path = Path().apply {
                moveTo(vertices.first().x, vertices.first().y)
                vertices.drop(1).forEach { lineTo(it.x, it.y) }
                close()
            }
            // Эффект свечения для рамки
            drawPath(path, color = emotion.color, style = Stroke(width = 8f, cap = StrokeCap.Round), alpha = 0.3f)
            drawPath(path, color = emotion.color, style = Stroke(width = 3f, cap = StrokeCap.Round), alpha = 1.0f)
        }

        // --- Остальные эмоции (код без изменений) ---
        is BasicEmotion.Sadness -> {
            val baseRadius = size.minDimension / 3f
            val rotationY = time * 0.5f; val rotationX = time * 0.3f
            val animatedPoints = points.map { point ->
                val pulse = sin(time * 1.5f + point.seed) * 15f
                val animatedRadius = baseRadius + pulse
                var p = point.copy(x = point.x * animatedRadius, y = point.y * animatedRadius, z = point.z * animatedRadius)
                p = p.rotateY(rotationY); p = p.rotateX(rotationX)
                p
            }
            val animatedPointsMap = points.zip(animatedPoints).toMap()
            connections.forEach { (startPoint, neighbors) ->
                val animatedStart = animatedPointsMap[startPoint]!!
                neighbors.forEach { endPoint ->
                    val animatedEnd = animatedPointsMap[endPoint]!!
                    val start2D = center + Offset(animatedStart.x, animatedStart.y); val end2D = center + Offset(animatedEnd.x, animatedEnd.y)
                    val alpha = mapValue((animatedStart.z + animatedEnd.z) / 2f, -baseRadius, baseRadius, 0.1f, 0.8f)
                    drawLine(color = emotion.color, start = start2D, end = end2D, strokeWidth = 1.5f, alpha = alpha.coerceIn(0f, 1f))
                }
            }
        }
        is BasicEmotion.Joy -> {
            val pulseValue = 1.0f + (sin(time * 5f) + 1f) / 2f * 0.1f
            withTransform({ scale(scale = pulseValue, pivot = center) }) {
                drawPath(createStarPath(center, 5, size.minDimension / 4f, size.minDimension / 8f), color = emotion.color)
            }
        }
        is BasicEmotion.Anger -> {
            repeat(20) {
                val angle = Random.nextFloat() * 2 * PI.toFloat()
                val length = size.minDimension / 2f * (0.5f + Random.nextFloat() * 0.5f)
                val endX = center.x + length * cos(angle); val endY = center.y + length * sin(angle)
                drawLine(color = emotion.color, start = center, end = Offset(endX, endY), strokeWidth = (1f + Random.nextFloat() * 4f))
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

// --- ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ---

// ДОБАВЛЕНА НЕДОСТАЮЩАЯ ФУНКЦИЯ
private fun getPolygonVertices(center: Offset, sides: Int, radius: Float): List<Offset> {
    val vertices = mutableListOf<Offset>()
    val angleStep = (2 * PI / sides).toFloat()
    for (i in 0 until sides) {
        val angle = i * angleStep - (PI / 2).toFloat()
        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)
        vertices.add(Offset(x, y))
    }
    return vertices
}

private fun createStarPath(center: Offset, points: Int, outerRadius: Float, innerRadius: Float): Path {
    val path = Path()
    val angleStep = (2 * PI) / (points * 2)
    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - (PI / 2)
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun createPolygonPath(center: Offset, sides: Int, radius: Float): Path {
    val path = Path()
    val angleStep = (2 * PI / sides).toFloat()
    for (i in 0 until sides) {
        val angle = i * angleStep - (PI / 2).toFloat()
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
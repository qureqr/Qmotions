package org.qure.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import org.qure.data.Emotions.*
import kotlin.math.*
import kotlin.random.Random

// Класс для луча, хранит направление, время рождения и длительность жизни
data class Ray(val direction: Point3D, val startTime: Float, val duration: Float)

// --- Константы для 3D-фигуры (Икосаэдр) ---
private val ICO_VERTICES = run {
    val t = (1.0f + sqrt(5.0f)) / 2.0f // Золотое сечение
    listOf(
        Point3D(-1f, t, 0f, 0f), Point3D(1f, t, 0f, 0f), Point3D(-1f, -t, 0f, 0f), Point3D(1f, -t, 0f, 0f),
        Point3D(0f, -1f, t, 0f), Point3D(0f, 1f, t, 0f), Point3D(0f, -1f, -t, 0f), Point3D(0f, 1f, -t, 0f),
        Point3D(t, 0f, -1f, 0f), Point3D(t, 0f, 1f, 0f), Point3D(-t, 0f, -1f, 0f), Point3D(-t, 0f, 1f, 0f)
    )
}
private val ICO_EDGES = listOf(
    0 to 1, 0 to 5, 0 to 7, 0 to 10, 0 to 11, 1 to 5, 1 to 7, 1 to 8, 1 to 9, 2 to 3, 2 to 4, 2 to 6, 2 to 10, 2 to 11,
    3 to 4, 3 to 6, 3 to 8, 3 to 9, 4 to 5, 4 to 9, 4 to 11, 5 to 9, 5 to 11, 6 to 7, 6 to 8, 6 to 10, 7 to 8, 7 to 10,
    8 to 9, 10 to 11
)

fun DrawScope.drawEmotion(
    emotion: Emotion,
    time: Float,
    points: List<Point3D>,
    connections: Map<Point3D, List<Point3D>>,
    rays: List<Ray>
) {
    when (emotion) {
        is BasicEmotion.Fear -> {
            val baseRadius = size.minDimension / 3.8f
            val rotationY = time * 0.4f
            val rotationX = time * 0.25f

            // 1. Вращаем и проецируем вершины икосаэдра
            val projectedVertices = ICO_VERTICES.map {
                var p = it.copy(x = it.x * baseRadius, y = it.y * baseRadius, z = it.z * baseRadius)
                p = p.rotateY(rotationY).rotateX(rotationX)
                center + Offset(p.x, p.y)
            }

            // 2. Рисуем "стреляющие" лучи
            rays.forEach { ray ->
                val age = time - ray.startTime
                val lifePercent = (age / ray.duration).coerceIn(0f, 1f)

                val currentLength = baseRadius * 1.55f * lifePercent
                val endPoint3D = ray.direction.copy(
                    x = ray.direction.x * currentLength,
                    y = ray.direction.y * currentLength,
                    z = ray.direction.z * currentLength
                ).rotateY(rotationY).rotateX(rotationX)

                val endPoint2D = center + Offset(endPoint3D.x, endPoint3D.y)
                val alpha = (1f - lifePercent) * 0.8f // Луч исчезает к концу жизни

                drawLine(color = emotion.color, start = center, end = endPoint2D, strokeWidth = 1.5f, alpha = alpha)
            }

            // 3. Рисуем ребра икосаэдра
            ICO_EDGES.forEach { (i, j) ->
                drawLine(color = emotion.color, start = projectedVertices[i], end = projectedVertices[j], strokeWidth = 2f, alpha = 0.5f)
            }
        }

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

// --- Вспомогательные функции ---

fun randomNormalizedPoint3D(): Point3D {
    val random = Random
    val theta = random.nextFloat() * 2 * PI.toFloat()
    val phi = acos(2 * random.nextFloat() - 1)
    return Point3D(
        x = sin(phi) * cos(theta),
        y = sin(phi) * sin(theta),
        z = cos(phi),
        seed = 0f
    )
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
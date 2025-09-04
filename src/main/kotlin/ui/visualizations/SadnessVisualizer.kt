package org.qure.ui.visualizations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.data.Sadness // Импортируем объект Sadness для доступа к его цвету
import org.qure.ui.shared.* // Импортируем Point3D, Ray и функции-расширения
import kotlin.math.sin

class SadnessVisualizer : EmotionVisualizer {
    override fun DrawScope.draw(
        time: Float,
        points: List<Point3D>,
        connections: Map<Point3D, List<Point3D>>,
        rays: List<Ray>
    ) {
        // --- Логика отрисовки "мнущейся" сферы ---
        val baseRadius = size.minDimension / 3f
        val rotationY = time * 0.5f
        val rotationX = time * 0.3f

        // Анимируем каждую точку индивидуально
        val animatedPoints = points.map { point ->
            val pulse = sin(time * 1.5f + point.seed) * 15f
            val animatedRadius = baseRadius + pulse
            var p = point.copy(x = point.x * animatedRadius, y = point.y * animatedRadius, z = point.z * animatedRadius)
            p = p.rotateY(rotationY)
            p = p.rotateX(rotationX)
            p
        }

        val animatedPointsMap = points.zip(animatedPoints).toMap()

        // Рисуем связи между точками
        connections.forEach { (startPoint, neighbors) ->
            val animatedStart = animatedPointsMap[startPoint]!!
            neighbors.forEach { endPoint ->
                val animatedEnd = animatedPointsMap[endPoint]!!
                val start2D = center + Offset(animatedStart.x, animatedStart.y)
                val end2D = center + Offset(animatedEnd.x, animatedEnd.y)

                // Используем Z-координату для имитации глубины (прозрачности)
                val alpha = mapValue((animatedStart.z + animatedEnd.z) / 2f, -baseRadius, baseRadius, 0.1f, 0.8f)

                drawLine(
                    color = Sadness.color, // Используем цвет напрямую из объекта эмоции
                    start = start2D,
                    end = end2D,
                    strokeWidth = 1.5f,
                    alpha = alpha.coerceIn(0f, 1f)
                )
            }
        }
    }
}
package org.qure.ui.visualizations
// NEUROCODE(Я ВАЩЕ НЕ ЕБУ В МАТЕМАТИКУ)
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.ui.shared.*
import kotlin.math.sin
import kotlin.math.sqrt

// Константы для 3D-фигуры (Икосаэдр) переезжают сюда
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

// 1. "Подписываем контракт" EmotionVisualizer
class FearVisualizer : EmotionVisualizer {

    // 2. Переопределяем обязательный метод draw
    override fun DrawScope.draw(
        time: Float,
        points: List<Point3D>,
        connections: Map<Point3D, List<Point3D>>,
        rays: List<Ray>,
        config: VisualizationConfig
    )  {
        // 3. Вставляем сюда ВСЮ логику отрисовки для Страха
        val baseRadius = size.minDimension / 3.5f
        val rotationY = time * 0.4f
        val rotationX = time * 0.25f

        // Вращаем и проецируем вершины икосаэдра
        val projectedVertices = ICO_VERTICES.map {
            var p = it.copy(x = it.x * baseRadius, y = it.y * baseRadius, z = it.z * baseRadius)
            p = p.rotateY(rotationY).rotateX(rotationX)
            center + Offset(p.x, p.y)
        }

        // Рисуем "стреляющие" лучи
        rays.forEach { ray ->
            val age = time - ray.startTime
            val lifePercent = (age / ray.duration).coerceIn(0f, 1f)

            val currentLength = baseRadius * 1.1f * lifePercent
            val endPoint3D = ray.direction.copy(
                x = ray.direction.x * currentLength,
                y = ray.direction.y * currentLength,
                z = ray.direction.z * currentLength
            ).rotateY(rotationY).rotateX(rotationX)

            val endPoint2D = center + Offset(endPoint3D.x, endPoint3D.y)
            val alpha = (1f - lifePercent) * 1.0f

            drawLine(color = Color(0xFF800080), start = center, end = endPoint2D, strokeWidth = 2.5f, alpha = alpha)
        }

        // Рисуем ребра икосаэдра
        ICO_EDGES.forEach { (i, j) ->
            drawLine(color = Color(0xFF800080), start = projectedVertices[i], end = projectedVertices[j], strokeWidth = 2f, alpha = 0.5f)
        }
    }
}

package org.qure.ui.visualizations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.data.Anger
import org.qure.ui.shared.*
import kotlin.math.*
import kotlin.random.Random

// --- ИСПРАВЛЕНА ОШИБКА ДЕСТРУКТУРИЗАЦИИ ---

// 1. Сначала генерируем данные и сохраняем их в одну переменную типа Pair
private val honeycombData = generateIcosphere(2)

// 2. Теперь безопасно извлекаем вершины и рёбра
private val HONEYCOMB_VERTICES = honeycombData.first
private val HONEYCOMB_EDGES = honeycombData.second.map { it[0] to it[1] } // Преобразуем IntArray в Pair

// Функция генерации геометрии (без изменений)
private fun generateIcosphere(subdivisions: Int): Pair<List<Point3D>, List<IntArray>> {
    val ICO_VERTICES_BASE = run {
        val t = (1.0f + sqrt(5.0f)) / 2.0f
        listOf(
            Point3D(-1f, t, 0f, 0f), Point3D(1f, t, 0f, 0f), Point3D(-1f, -t, 0f, 0f), Point3D(1f, -t, 0f, 0f),
            Point3D(0f, -1f, t, 0f), Point3D(0f, 1f, t, 0f), Point3D(0f, -1f, -t, 0f), Point3D(0f, 1f, -t, 0f),
            Point3D(t, 0f, -1f, 0f), Point3D(t, 0f, 1f, 0f), Point3D(-t, 0f, -1f, 0f), Point3D(-t, 0f, 1f, 0f)
        ).map { it.normalized() }
    }
    val ICO_FACES_BASE = listOf(
        0 to 11 to 5, 0 to 5 to 1, 0 to 1 to 7, 0 to 7 to 10, 0 to 10 to 11,
        1 to 5 to 9, 5 to 11 to 4, 11 to 10 to 2, 10 to 7 to 6, 7 to 1 to 8,
        3 to 9 to 4, 3 to 4 to 2, 3 to 2 to 6, 3 to 6 to 8, 3 to 8 to 9,
        4 to 9 to 5, 2 to 4 to 11, 6 to 2 to 10, 8 to 6 to 7, 9 to 8 to 1
    )

    var vertices = ICO_VERTICES_BASE.toMutableList()
    var faces = ICO_FACES_BASE.map { intArrayOf(it.first.first, it.first.second, it.second) }

    repeat(subdivisions) {
        val newFaces = mutableListOf<IntArray>()
        val midPointCache = mutableMapOf<Pair<Int, Int>, Int>()

        fun getMidPoint(p1: Int, p2: Int): Int {
            val key = if (p1 < p2) p1 to p2 else p2 to p1
            return midPointCache.getOrPut(key) {
                val point1 = vertices[p1]
                val point2 = vertices[p2]
                val mid = Point3D((point1.x + point2.x) / 2f, (point1.y + point2.y) / 2f, (point1.z + point2.z) / 2f, 0f).normalized()
                vertices.add(mid)
                vertices.lastIndex
            }
        }

        for (face in faces) {
            val v1 = face[0]; val v2 = face[1]; val v3 = face[2]
            val a = getMidPoint(v1, v2); val b = getMidPoint(v2, v3); val c = getMidPoint(v3, v1)
            newFaces.add(intArrayOf(v1, a, c)); newFaces.add(intArrayOf(v2, b, a))
            newFaces.add(intArrayOf(v3, c, b)); newFaces.add(intArrayOf(a, b, c))
        }
        faces = newFaces
    }
    // Генерируем рёбра из финальных треугольников
    val edges = mutableSetOf<IntArray>()
    for (face in faces) {
        for (i in face.indices) {
            val p1 = face[i]
            val p2 = face[(i + 1) % face.size]
            val edge = if (p1 < p2) intArrayOf(p1, p2) else intArrayOf(p2, p1)
            edges.add(edge)
        }
    }
    return vertices to edges.toList()
}


class AngerVisualizer : EmotionVisualizer {
    // Обновляем сигнатуру функции, чтобы она принимала config
    override fun DrawScope.draw(
        time: Float,
        points: List<Point3D>,
        connections: Map<Point3D, List<Point3D>>,
        rays: List<Ray>,
        config: VisualizationConfig
    ) {
        // --- Используем config для расчета размера ---
        val baseRadius = (size.minDimension / 2f) * config.baseScale

        // --- Остальная логика отрисовки (без изменений) ---
        val rotationY = time * 0.3f
        val rotationX = time * 0.2f
        val pulseSpeed = 8f; val pulsePower = 8f
        val pulse = abs(sin(time * pulseSpeed)).pow(pulsePower) * 0.2f + 1.0f
        val currentRadius = baseRadius * pulse
        val jitterAmount = (pulse - 1.0f) * 5f
        val projectedVertices = HONEYCOMB_VERTICES.map {
            val jitterX = (Random.nextFloat() - 0.5f) * jitterAmount
            val jitterY = (Random.nextFloat() - 0.5f) * jitterAmount
            var p = it.copy(x = it.x * currentRadius, y = it.y * currentRadius, z = it.z * currentRadius)
            p = p.rotateY(rotationY).rotateX(rotationX)
            center + Offset(p.x + jitterX, p.y + jitterY)
        }
        val strokeWidth = 2.5f + (pulse - 1.0f) * 5f
        val color = Anger.color.copy(red = 0.7f + (pulse - 1.0f) * 1.5f)
        HONEYCOMB_EDGES.forEach { (i, j) ->
            if (i < projectedVertices.size && j < projectedVertices.size) {
                drawLine(color = color, start = projectedVertices[i], end = projectedVertices[j], strokeWidth = strokeWidth)
            }
        }
    }
}

private fun Point3D.normalized(): Point3D {
    val len = sqrt(x*x + y*y + z*z)
    return if (len == 0f) this else this.copy(x = x/len, y = y/len, z = z/len)
}
package org.qure.ui.visualizations

import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.ui.shared.Point3D
import org.qure.ui.shared.Ray

/**
 * Интерфейс ("контракт") для всех визуализаторов эмоций.
 * Он определяет одно единственное правило: у каждого визуализатора
 * должна быть функция `draw`, которая умеет рисовать на холсте (DrawScope),
 * используя переданные ей данные (время, точки, лучи и т.д.).
 */
interface EmotionVisualizer {
    fun DrawScope.draw(
        time: Float,
        points: List<Point3D>,
        connections: Map<Point3D, List<Point3D>>,
        rays: List<Ray>
    )
}
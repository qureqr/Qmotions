package org.qure.ui

import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.data.Emotion // ИСПРАВЛЕНО: Импортируем интерфейс Emotion (в ед. числе)
import org.qure.ui.shared.Point3D
import org.qure.ui.shared.Ray

/**
 * Главный диспетчер отрисовки.
 * Вызывает персональный визуализатор для переданной эмоции.
 */
fun DrawScope.renderEmotion(
    emotion: Emotion, // ИСПРАВЛЕНО: Тип теперь Emotion (в ед. числе)
    time: Float,
    points: List<Point3D>,
    connections: Map<Point3D, List<Point3D>>,
    rays: List<Ray>
) {
    // Просто просим эмоцию нарисовать себя с помощью её личного визуализатора
    with(emotion.visualizer) {
        draw(time, points, connections, rays)
    }
}
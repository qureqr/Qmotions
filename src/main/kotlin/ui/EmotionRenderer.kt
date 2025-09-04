package org.qure.ui

import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.data.Emotion
import org.qure.ui.shared.Point3D
import org.qure.ui.shared.Ray
import org.qure.ui.shared.VisualizationConfig

/**
 * Главный диспетчер отрисовки.
 * Вызывает персональный визуализатор для переданной эмоции.
 */
fun DrawScope.renderEmotion(
    emotion: Emotion,
    time: Float,
    points: List<Point3D>,
    connections: Map<Point3D, List<Point3D>>,
    rays: List<Ray>,
    config: VisualizationConfig
) {
    with(emotion.visualizer) {
        draw(time, points, connections, rays, config)
    }
}
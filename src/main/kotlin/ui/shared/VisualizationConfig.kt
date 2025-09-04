package org.qure.ui.shared

/**
 * Хранит общие параметры для всех визуализаций.
 * @param baseScale Базовый масштаб фигуры (от 0.0 до 1.0). 0.8 означает,
 * что фигура будет занимать 80% от минимального размера холста.
 */
data class VisualizationConfig(
    val baseScale: Float = 0.8f
)
package org.qure.data

import androidx.compose.ui.graphics.Color
import org.qure.ui.visualizations.EmotionVisualizer
import org.qure.ui.visualizations.FearVisualizer
import org.qure.ui.visualizations.SadnessVisualizer
import org.qure.ui.visualizations.AngerVisualizer

/**
 * Базовый интерфейс для всех эмоций.
 */
interface Emotion {
    val name: String
    val color: Color
    val visualizer: EmotionVisualizer
}

/**
 * Определение эмоции "Страх".
 * Содержит свои данные и ссылку на свой персональный "рисунок".
 */
object Fear : Emotion {
    override val name: String = "Страх"
    override val color: Color = Color(0xFF800080) // Пурпурный
    override val visualizer: EmotionVisualizer = FearVisualizer()
}

/**
 * Определение эмоции "Грусть".
 */
object Sadness : Emotion {
    override val name: String = "Грусть"
    override val color: Color = Color(0xFF6495ED) // Васильковый
    override val visualizer: EmotionVisualizer = SadnessVisualizer()
}

/**
 * Определение эмоции "Гнев".
 **/
object Anger : Emotion {
    override val name: String = "Гнев"
    override val color: Color = Color(0xFFB30000) // Глубокий красный
    override val visualizer: EmotionVisualizer = AngerVisualizer()
}
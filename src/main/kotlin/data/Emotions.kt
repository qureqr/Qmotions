package org.qure.data.Emotions

import androidx.compose.ui.graphics.Color

interface Emotion {
    val name: String
    val color: Color
}

sealed class BasicEmotion(
    override val name: String,
    override val color: Color,
    val intensity: Float
) : Emotion {
    data object Joy : BasicEmotion("Радость", Color(0xFFFFFFA0), 0.8f)
    data object Trust : BasicEmotion("Доверие", Color(0xFF90EE90), 0.7f)
    data object Fear : BasicEmotion("Страх", Color(0xFF800080), 0.8f)
    data object Surprise : BasicEmotion("Удивление", Color(0xFF00FFFF), 0.9f)
    data object Sadness : BasicEmotion("Грусть", Color(0xFF6495ED), 0.6f)
    data object Disgust : BasicEmotion("Отвращение", Color(0xFF808000), 0.7f)
    data object Anger : BasicEmotion("Гнев", Color(0xFFDC143C), 0.9f)
    data object Anticipation : BasicEmotion("Ожидание", Color(0xFFFFA500), 0.6f)
    data object Calmness : BasicEmotion("Спокойствие", Color(0xFFADD8E6), 0.5f)
    data object Neutral : BasicEmotion("Нейтральность", Color.Gray, 0.2f)
}

data class ComplexEmotion(
    override val name: String,
    val emotions: List<Emotion>
) : Emotion {
    override val color: Color
        get() {
            if (emotions.isEmpty()) return Color.Gray
            val totalRed = emotions.sumOf { it.color.red.toDouble() }
            val totalGreen = emotions.sumOf { it.color.green.toDouble() }
            val totalBlue = emotions.sumOf { it.color.blue.toDouble() }
            val count = emotions.size.toFloat()
            return Color(
                red = (totalRed / count).toFloat(),
                green = (totalGreen / count).toFloat(),
                blue = (totalBlue / count).toFloat()
            )
        }
}
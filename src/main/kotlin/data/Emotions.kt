package org.qure.data.Emotions

import androidx.compose.ui.graphics.Color

/**
 * Общий интерфейс для всех эмоций.
 * Каждая эмоция имеет имя и основной цвет.
 */
interface Emotion {
    val name: String
    val color: Color
}

/**
 * Запечатанный класс для базовых, "чистых" эмоций.
 * Sealed class гарантирует, что мы не забудем обработать какой-либо
 * из типов в `when` выражении.
 * @param intensity Условная интенсивность эмоции, которую можно использовать для визуализации.
 */
sealed class BasicEmotion(
    override val name: String,
    override val color: Color,
    val intensity: Float
) : Emotion {
    data object Calmness : BasicEmotion("Спокойствие", Color(0xFFADD8E6), 0.5f) // Светло-голубой
    data object Joy : BasicEmotion("Радость", Color(0xFFFFFFA0), 0.8f)      // Светло-желтый
    data object Sadness : BasicEmotion("Грусть", Color(0xFF6495ED), 0.6f)    // Васильковый
    data object Anger : BasicEmotion("Злость" , Color(0xFFDC143C), 0.9f)       // Малиновый
    data object Neutral : BasicEmotion("Нейтральность", Color.Gray, 0.2f)
    data object Trust : BasicEmotion("Доверие", Color(0xFF90EE90), 0.7f)
    data object Fear : BasicEmotion("Страх", Color(0xFF800080), 0.8f)             // Пурпурный
    data object Disgust : BasicEmotion("Отвращение", Color(0xFF808000), 0.7f)    // Оливковый)
    data object Surprise : BasicEmotion("Удивление", Color(0xFF00FFFF), 0.9f)   // Бирюзовый
    data object Anticipation : BasicEmotion("Ожидание", Color(0xFFFFA500), 0.6f) // Оранжевый



}

/**
 * Класс для сложных, составных эмоций.
 * @param emotions Список эмоций, из которых состоит эта сложная эмоция.
 */
data class ComplexEmotion(
    override val name: String,
    val emotions: List<Emotion>
) : Emotion {
    // Цвет сложной эмоции вычисляется как среднее арифметическое цветов ее компонентов.
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
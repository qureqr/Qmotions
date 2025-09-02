// отрисовочка
package org.qure.ui


import androidx.compose.ui.graphics.drawscope.DrawScope
import org.qure.data.Emotions.*

fun DrawScope.drawEmotion(emotion: Emotion) {
    // Используем when для выбора логики отрисовки
    when (emotion) {
        is BasicEmotion.Calmness -> {
            drawLine(
                color = emotion.color,
                start = center.copy(x = 0f),
                end = center.copy(x = size.width),
                strokeWidth = 5f
            )
        }
        is BasicEmotion.Joy -> {
            // Пятиконечник сложнее, его нужно рисовать по точкам
            // Пока просто нарисуем круг для примера
            drawCircle(color = emotion.color, radius = size.minDimension / 4)
        }
        is BasicEmotion.Anger -> {
            // Хаотичные линии из центра
            repeat(10) {
                // Тут понадобится немного математики для случайных линий
            }
        }
        is ComplexEmotion -> {
            // Для сложных эмоций можно комбинировать визуализации!
            // Например, нарисовать фон от первой эмоции, а фигуры от второй.
            // Пока просто нарисуем несколько концентрических кругов
            emotion.emotions.forEachIndexed { index, subEmotion ->
                drawCircle(
                    color = subEmotion.color,
                    radius = (size.minDimension / 3) - (index * 30),
                    alpha = 0.5f // Сделаем их полупрозрачными
                )
            }
        }
        // ... другие эмоции
        else -> {
            // Заглушка для нереализованных эмоций
            drawCircle(color = emotion.color, radius = 20f)
        }
    }
}
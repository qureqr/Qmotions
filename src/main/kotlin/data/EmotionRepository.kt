package org.qure.data

import org.qure.data.Emotions.*

/**
 * Объект-репозиторий, который хранит все известные приложению эмоции
 * и предоставляет метод для их поиска по имени.
 */
object EmotionRepository {
    // Приватный словарь, где ключ - название эмоции в нижнем регистре, а значение - объект эмоции.
    private val emotions: Map<String, Emotion> = mapOf(
        "спокойствие" to BasicEmotion.Calmness,
        "радость" to BasicEmotion.Joy,
        "грусть" to BasicEmotion.Sadness,
        "гнев" to BasicEmotion.Anger,
        "нейтральность" to BasicEmotion.Neutral,
        "апатия" to ComplexEmotion("Апатия", listOf(BasicEmotion.Sadness, BasicEmotion.Neutral)),
        "восторг" to ComplexEmotion("Восторг", listOf(BasicEmotion.Joy, BasicEmotion.Joy, BasicEmotion.Calmness))
    )

    /**
     * Находит эмоцию по имени. Поиск не чувствителен к регистру.
     * @param name Название эмоции для поиска.
     * @return Объект [Emotion] если найден, иначе null.
     */
    fun findEmotion(name: String): Emotion? {
        return emotions[name.trim().lowercase()]
    }
}
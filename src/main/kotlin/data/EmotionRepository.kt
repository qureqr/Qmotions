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
        "умиротворение" to BasicEmotion.Calmness,

        "нейтральность" to BasicEmotion.Neutral,

        "радость" to BasicEmotion.Joy,

        "грусть" to BasicEmotion.Sadness,
        "печаль" to BasicEmotion.Sadness,

        "гнев" to BasicEmotion.Anger,
        "злость" to BasicEmotion.Anger,

        "нейтральность" to BasicEmotion.Neutral,

        "страх" to BasicEmotion.Fear,
        "испуг" to BasicEmotion.Fear,

        "удивление" to BasicEmotion.Surprise,

        "отвращение" to BasicEmotion.Disgust,
        "омерзение" to BasicEmotion.Disgust,

        "ожидание" to BasicEmotion.Anticipation,
        "предвкушение" to BasicEmotion.Anticipation,

        "любовь" to ComplexEmotion("Любовь", listOf(BasicEmotion.Joy, BasicEmotion.Trust)),
        "оптимизм" to ComplexEmotion("Оптимизм", listOf(BasicEmotion.Anticipation, BasicEmotion.Joy)),
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
package org.qure.data

import org.qure.data.Emotions.*

object EmotionRepository {
    private val emotions: Map<String, Emotion> = mapOf(
        // Базовые эмоции и их синонимы
        "радость" to BasicEmotion.Joy, "счастье" to BasicEmotion.Joy,
        "доверие" to BasicEmotion.Trust,
        "страх" to BasicEmotion.Fear, "испуг" to BasicEmotion.Fear,
        "удивление" to BasicEmotion.Surprise,
        "грусть" to BasicEmotion.Sadness, "печаль" to BasicEmotion.Sadness,
        "отвращение" to BasicEmotion.Disgust, "омерзение" to BasicEmotion.Disgust,
        "гнев" to BasicEmotion.Anger, "злость" to BasicEmotion.Anger, "ярость" to BasicEmotion.Anger,
        "ожидание" to BasicEmotion.Anticipation, "предвкушение" to BasicEmotion.Anticipation,

        // Вспомогательные
        "спокойствие" to BasicEmotion.Calmness, "умиротворение" to BasicEmotion.Calmness,
        "нейтральность" to BasicEmotion.Neutral,

        // Сложные эмоции
        "любовь" to ComplexEmotion("Любовь", listOf(BasicEmotion.Joy, BasicEmotion.Trust)),
        "оптимизм" to ComplexEmotion("Оптимизм", listOf(BasicEmotion.Anticipation, BasicEmotion.Joy)),
        "апатия" to ComplexEmotion("Апатия", listOf(BasicEmotion.Sadness, BasicEmotion.Neutral)),
    )

    fun findEmotion(name: String): Emotion? {
        return emotions[name.trim().lowercase()]
    }
}
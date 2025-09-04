package org.qure.data

object EmotionRepository {
    private val emotionsMap: Map<String, Emotion> = mapOf(
        "страх" to Fear,
        "испуг" to Fear
    )

    fun findEmotion(name: String): Emotion? {
        return emotionsMap[name.trim().lowercase()]
    }
}
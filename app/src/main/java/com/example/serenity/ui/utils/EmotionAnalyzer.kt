package com.example.serenity.ui.utils

import com.example.serenity.data.model.Mood

object EmotionAnalyzer {

    fun analyzeMood(moods: List<Mood>): String {
        if (moods.isEmpty()) {
            return "Aún no hay datos emocionales. ¡Registra cómo te sientes!"
        }

        // 1. Analizar el estado ACTUAL (la última cara seleccionada)
        val lastMood = moods.last()
        val currentAnalysis = when (lastMood.emotion) {
            "Feliz"   -> "😀 Actualmente te sientes feliz."
            "Bien"    -> "🙂 Actualmente te sientes bien."
            "Neutral" -> "😐 Actualmente te sientes neutral."
            "Triste"  -> "😔 Actualmente te sientes triste."
            "Ansioso" -> "😰 Actualmente te sientes ansioso."
            else      -> "Registraste un estado de ánimo."
        }

        // 2. Analizar la TENDENCIA (comparar la última cara con la penúltima)
        val trend = if (moods.size >= 2) {
            val prevMood = moods[moods.size - 2]
            val prevValue = getMoodValue(prevMood.emotion)
            val lastValue = getMoodValue(lastMood.emotion)

            when {
                lastValue > prevValue -> "📈 Tu estado emocional ha mejorado respecto al registro anterior."
                lastValue < prevValue -> "📉 Tu estado emocional ha disminuido respecto al registro anterior. Recuerda que es normal tener altibajos."
                else                  -> "➡ Tu estado emocional se mantiene estable respecto al registro anterior."
            }
        } else {
            "Registra otro estado de ánimo para ver tu evolución."
        }

        return "$currentAnalysis\n$trend"
    }

    // Función auxiliar para asignar valores numéricos
    private fun getMoodValue(emotion: String): Int = when (emotion) {
        "Feliz"   -> 5
        "Bien"    -> 4
        "Neutral" -> 3
        "Triste"  -> 2
        "Ansioso" -> 1
        else      -> 0
    }
}
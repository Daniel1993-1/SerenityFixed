package com.example.serenity.ui.utils

import com.example.serenity.data.model.Mood

object EmotionAnalyzer {

    fun analyzeMood(moods: List<Mood>): String {

        if (moods.isEmpty()) {
            return "Aún no hay datos emocionales."
        }

        val values = moods.map {
            when (it.emotion) {
                "Feliz"   -> 5
                "Bien"    -> 4
                "Neutral" -> 3
                "Triste"  -> 2
                "Ansioso" -> 1
                else      -> 0
            }
        }

        val average = values.average()

        val trend = if (values.size >= 2) {
            val first = values.first()
            val last  = values.last()
            when {
                last > first -> "📈 Tu estado emocional está mejorando."
                last < first -> "📉 Tu estado emocional ha disminuido."
                else         -> "➡ Tu estado emocional se mantiene estable."
            }
        } else {
            ""
        }

        val analysis = when {
            average >= 4 -> "😀 Tu estado emocional es positivo esta semana."
            average >= 3 -> "🙂 Tu estado emocional es estable."
            average >= 2 -> "😐 Se detectan señales de estrés. Considera descansar o hacer ejercicio."
            else         -> "😔 Tu estado emocional está bajo. Se recomienda hablar con alguien o practicar relajación."
        }

        return "$analysis\n$trend"
    }
}
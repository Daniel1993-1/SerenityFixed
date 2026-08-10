package com.example.serenity.data.recommendation

object RecommendationEngine {

    fun getRecommendations(emotion: String): List<String> {

        return when (emotion) {

            "Ansioso" -> listOf(
                "Respiración guiada 4-7-8",
                "Meditación corta de 3 minutos",
                "Caminar 10 minutos",
                "Escuchar música relajante"
            )

            "Triste" -> listOf(
                "Hablar con un amigo",
                "Escribir lo que sientes",
                "Escuchar música positiva",
                "Hacer actividad física ligera"
            )

            "Neutral" -> listOf(
                "Tomar una pausa consciente",
                "Estiramientos",
                "Respirar profundamente"
            )

            "Bien" -> listOf(
                "Mantener tu rutina saludable",
                "Agradecer tres cosas del día",
                "Actividad física"
            )

            "Feliz" -> listOf(
                "Compartir tu alegría con alguien",
                "Registrar algo positivo del día",
                "Aprovechar tu energía en algo creativo"
            )

            else -> listOf("Tómate un momento para respirar")
        }
    }
}
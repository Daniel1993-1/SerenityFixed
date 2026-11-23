package com.example.serenity.ui.questionnaire

object PsicometricCalculator {

    fun getGAD7SeverityLabel(score: Int): String = when {
        score <= 4  -> "minima"
        score <= 9  -> "leve"
        score <= 14 -> "moderada"
        else        -> "severa"
    }

    fun getPHQ9SeverityLabel(score: Int): String = when {
        score <= 4  -> "minima"
        score <= 9  -> "leve"
        score <= 14 -> "moderada"
        score <= 19 -> "moderada_severa"
        else        -> "severa"
    }

    fun calculatePSS10Score(answers: List<Int>): Int {
        val invertedItems = setOf(3, 4, 6, 7)
        return answers.mapIndexed { index, value ->
            if (index in invertedItems) 4 - value else value
        }.sum()
    }


    fun getPSSSeverityLabel(score: Int): String = "puntaje_continuo"
}
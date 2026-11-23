package com.example.serenity

import com.example.serenity.ui.questionnaire.PsicometricCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PsicometricCalculationTest {

    // ── CASOS GAD-7 ──

    // C-01: Todas las respuestas en 0
    @Test
    fun `C-01 GAD7 todas las respuestas en 0`() {
        val score = List(7) { 0 }.sum()
        assertEquals(0, score)
        assertEquals("minima", PsicometricCalculator.getGAD7SeverityLabel(score))
    }

    // C-02: Límites de rango (4, 5, 9, 10, 14, 15)
    @Test
    fun `C-02 GAD7 limites de rango`() {
        assertEquals("minima", PsicometricCalculator.getGAD7SeverityLabel(4))
        assertEquals("leve", PsicometricCalculator.getGAD7SeverityLabel(5))
        assertEquals("leve", PsicometricCalculator.getGAD7SeverityLabel(9))
        assertEquals("moderada", PsicometricCalculator.getGAD7SeverityLabel(10))
        assertEquals("moderada", PsicometricCalculator.getGAD7SeverityLabel(14))
        assertEquals("severa", PsicometricCalculator.getGAD7SeverityLabel(15))
    }

    // C-03: Todas las respuestas en 3
    @Test
    fun `C-03 GAD7 todas las respuestas en 3`() {
        val score = List(7) { 3 }.sum() // 21
        assertEquals(21, score)
        assertEquals("severa", PsicometricCalculator.getGAD7SeverityLabel(score))
    }

    // ── CASOS PHQ-9 ──

    // C-04: Todas las respuestas en 0
    @Test
    fun `C-04 PHQ9 todas las respuestas en 0`() {
        val score = List(9) { 0 }.sum()
        assertEquals(0, score)
        assertEquals("minima", PsicometricCalculator.getPHQ9SeverityLabel(score))
    }

    // C-05: Límites de rango (4, 5, 9, 10, 14, 15, 19, 20)
    @Test
    fun `C-05 PHQ9 limites de rango`() {
        assertEquals("minima", PsicometricCalculator.getPHQ9SeverityLabel(4))
        assertEquals("leve", PsicometricCalculator.getPHQ9SeverityLabel(5))
        assertEquals("leve", PsicometricCalculator.getPHQ9SeverityLabel(9))
        assertEquals("moderada", PsicometricCalculator.getPHQ9SeverityLabel(10))
        assertEquals("moderada", PsicometricCalculator.getPHQ9SeverityLabel(14))
        assertEquals("moderada_severa", PsicometricCalculator.getPHQ9SeverityLabel(15))
        assertEquals("moderada_severa", PsicometricCalculator.getPHQ9SeverityLabel(19))
        assertEquals("severa", PsicometricCalculator.getPHQ9SeverityLabel(20))
    }

    // C-06: Todas las respuestas en 3
    @Test
    fun `C-06 PHQ9 todas las respuestas en 3`() {
        val score = List(9) { 3 }.sum() // 27
        assertEquals(27, score)
        assertEquals("severa", PsicometricCalculator.getPHQ9SeverityLabel(score))
    }

    // ── CASOS PSS-10 ──

    // C-07: 0 en ítems positivos y 4 en negativos (verifica inversión)
    @Test
    fun `C-07 PSS10 con 0 en items positivos y 4 en negativos`() {
        val answers = listOf(0, 0, 0, 4, 4, 0, 4, 4, 0, 0)
        val score = PsicometricCalculator.calculatePSS10Score(answers)
        assertEquals(0, score)
    }

    // C-08: 4 en ítems positivos y 0 en negativos
    @Test
    fun `C-08 PSS10 con 4 en items positivos y 0 en negativos`() {
        val answers = listOf(4, 4, 4, 0, 0, 4, 0, 0, 4, 4)
        val score = PsicometricCalculator.calculatePSS10Score(answers)
        assertEquals(40, score)
    }

    // C-09: Todas las respuestas en 0 y luego todas en 4
    @Test
    fun `C-09 PSS10 extremos`() {
        // Todas en 0
        val answersZero = List(10) { 0 }
        // Cálculo: 6 ítems normales (0) + 4 ítems invertidos (4-0=4) = 16
        assertEquals(16, PsicometricCalculator.calculatePSS10Score(answersZero))

        // Todas en 4
        val answersFour = List(10) { 4 }
        // Cálculo: 6 ítems normales (4) + 4 ítems invertidos (4-4=0) = 24
        assertEquals(24, PsicometricCalculator.calculatePSS10Score(answersFour))
    }

    // C-10: PSS-10 se reporta como puntaje continuo (0-40), sin categorías clínicas
    @Test
    fun `C-10 PSS10 solo puntaje sin categoria clinica`() {
        // Verificamos que el cálculo devuelva el valor exacto sin importar la categoría
        val answersTest = List(10) { 2 } // 6 ítems normales (2) + 4 ítems invertidos (4-2=2)
        assertEquals(20, PsicometricCalculator.calculatePSS10Score(answersTest))


        assertEquals("puntaje_continuo", PsicometricCalculator.getPSSSeverityLabel(20))
    }
}
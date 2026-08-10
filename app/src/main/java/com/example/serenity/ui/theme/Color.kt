package com.example.serenity.ui.theme

import androidx.compose.ui.graphics.Color

// ══════════════════════════════════════════════════════════════════════════════
// SERENITY — Paleta "Aurora Terapéutica"
// ══════════════════════════════════════════════════════════════════════════════

// ── Fondo unificado navy ──────────────────────────────────────────────────────
// CAMBIO 1: Todas las secciones usan el mismo navy #0D1B2A
// Se eliminaron los verdes (ProgressBgStart, ProgressBgEnd, LoginBgEnd)
val NavyBg           = Color(0xFF0D1B2A)   // ← fondo único de toda la app

val HomeBgStart      = NavyBg
val HomeBgEnd        = Color(0xFF0F2030)

val TestBgStart      = NavyBg              // antes: 0xFF120D2E (índigo)
val TestBgEnd        = Color(0xFF0F1E32)   // antes: 0xFF0D1E3D

val ProgressBgStart  = NavyBg              // antes: 0xFF091E14 (verde bosque) ← eliminado
val ProgressBgEnd    = Color(0xFF0F2030)   // antes: 0xFF0E1C2A (verde-azul)   ← eliminado

val ResourceBgStart  = NavyBg              // antes: 0xFF150D2E
val ResourceBgEnd    = Color(0xFF0F1A2E)

val ProfileBgStart   = NavyBg
val ProfileBgEnd     = Color(0xFF121830)

val LoginBgStart     = NavyBg              // antes: 0xFF060E1A
val LoginBgEnd       = Color(0xFF0F2030)   // antes: 0xFF091E14 (verde) ← eliminado

// ── Accent Principal — Teal ───────────────────────────────────────────────────
val TealStart        = Color(0xFF4ECDC4)
val TealEnd          = Color(0xFF38B2AC)
val TealDeep         = Color(0xFF2C9E97)

// ── Accent Secundario — Verde salvia ──────────────────────────────────────────
val SageStart        = Color(0xFF7EC8A4)
val SageEnd          = Color(0xFF56A97E)

// ── Aurora — Violeta → Azul ───────────────────────────────────────────────────
val AuroraStart      = Color(0xFF7B8FE8)
val AuroraEnd        = Color(0xFF5B72D4)
val AuroraMid        = Color(0xFF6E7FDC)

// ── Lavanda ───────────────────────────────────────────────────────────────────
val LavenderStart    = Color(0xFFB48EF0)
val LavenderEnd      = Color(0xFF9B72E8)
val LavenderDeep     = Color(0xFF7B52C8)

// ── Coral ─────────────────────────────────────────────────────────────────────
val CoralStart       = Color(0xFFFF9F7F)
val CoralEnd         = Color(0xFFFF7F5E)

// ── Dorado ────────────────────────────────────────────────────────────────────
val GoldStart        = Color(0xFFFFD166)
val GoldEnd          = Color(0xFFFFC233)

// ── Glassmorphism ─────────────────────────────────────────────────────────────
val SurfaceGlass         = Color(0x1AFFFFFF)
val SurfaceGlassMedium   = Color(0x26FFFFFF)
val SurfaceGlassStrong   = Color(0x33FFFFFF)
val SurfaceGlassDark     = Color(0x1A000000)
val BorderGlass          = Color(0x33FFFFFF)
val BorderGlassSubtle    = Color(0x1AFFFFFF)

// ── Texto ─────────────────────────────────────────────────────────────────────
val TextWhite        = Color(0xFFF0F4F8)
val TextWhite70      = Color(0xB3F0F4F8)
val TextWhite50      = Color(0x80F0F4F8)
val TextWhite30      = Color(0x4DF0F4F8)

// ── CAMBIO 3: Objeto unificado de severidad ───────────────────────────────────
// Úsalo en TODAS las pantallas: SeverityColors.forScore(score, testType)
// Reemplaza cualquier when/if disperso en Perfil y Progreso
object SeverityColors {
    // Colores base (sin cambios de valor, solo centralizados)
    val Minimal  = Color(0xFF06D6A0)   // Verde jade
    val Mild     = Color(0xFF7EC8A4)   // Verde salvia
    val Moderate = Color(0xFFFFB347)   // Ámbar suave
    val High     = Color(0xFFFF8C69)   // Coral
    val Severe   = Color(0xFFE63946)   // Rojo

    /**
     * Devuelve el color correspondiente al nivel de severidad de PHQ-9.
     * PHQ-9: 0-4 Minimal | 5-9 Mild | 10-14 Moderate | 15-19 High | 20-27 Severe
     */
    fun forPhq9(score: Int): Color = when {
        score <= 4  -> Minimal
        score <= 9  -> Mild
        score <= 14 -> Moderate
        score <= 19 -> High
        else        -> Severe
    }

    /**
     * Devuelve el color correspondiente al nivel de severidad de GAD-7.
     * GAD-7: 0-4 Minimal | 5-9 Mild | 10-14 Moderate | 15-21 Severe
     */
    fun forGad7(score: Int): Color = when {
        score <= 4  -> Minimal
        score <= 9  -> Mild
        score <= 14 -> Moderate
        else        -> Severe
    }

    /**
     * Devuelve el color correspondiente al nivel de severidad de PSS-10.
     * PSS-10: 0-13 Minimal | 14-26 Moderate | 27-40 Severe
     */
    fun forPss10(score: Int): Color = when {
        score <= 13 -> Minimal
        score <= 26 -> Moderate
        else        -> Severe
    }

    /** Genérico por etiqueta de texto si ya tienes el label calculado */
    fun forLabel(label: String): Color = when (label.lowercase()) {
        "mínimo", "minimo", "minimal"   -> Minimal
        "leve", "mild"                  -> Mild
        "moderado", "moderate"          -> Moderate
        "moderado-severo", "high"       -> High
        "severo", "severe"              -> Severe
        else                            -> Minimal
    }
}

// ── Severidad individual (alias para compatibilidad con código existente) ──────
val SeverityMinimal  = SeverityColors.Minimal
val SeverityMild     = SeverityColors.Mild
val SeverityModerate = SeverityColors.Moderate
val SeverityHigh     = SeverityColors.High
val SeveritySevere   = SeverityColors.Severe

// ── Ánimo ─────────────────────────────────────────────────────────────────────
val MoodExcellent    = Color(0xFF06D6A0)
val MoodGood         = Color(0xFF4ECDC4)
val MoodNeutral      = Color(0xFFFFD166)
val MoodSad          = Color(0xFF7B8FE8)
val MoodAnxious      = Color(0xFFFF9F7F)

// ── Material3 baseline ────────────────────────────────────────────────────────
val Purple80         = Color(0xFFD0BCFF)
val PurpleGrey80     = Color(0xFFCCC2DC)
val Pink80           = Color(0xFFEFB8C8)
val Purple40         = Color(0xFF6650A4)
val PurpleGrey40     = Color(0xFF625B71)
val Pink40           = Color(0xFF7D5260)

// ── Alerta preventiva ─────────────────────────────────────────────────────────
val AlertBackground  = Color(0x1AFF8C69)
val AlertBorder      = Color(0x4DFF8C69)

// ── CAMBIO 4: Botón CTA Login — teal menos saturado ──────────────────────────
// Antes usaba TealStart (0xFF4ECDC4) directo, muy vibrante en fondo oscuro.
// Estos dos valores van en el gradiente del botón principal de LoginScreen.
val CtaPrimaryStart  = Color(0xFF3AAFA9)   // teal desaturado, más sobrio
val CtaPrimaryEnd    = Color(0xFF2E8F8A)   // teal profundo, menos eléctrico
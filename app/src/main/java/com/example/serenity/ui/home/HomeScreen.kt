package com.example.serenity.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenity.data.recommendation.RecommendationEngine
import com.example.serenity.ui.MoodViewModel
import com.example.serenity.ui.components.EmotionChart
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*
import com.example.serenity.ui.utils.EmotionAnalyzer

// ── Paleta clínica por instrumento ────────────────────────────────────────────
// PHQ-9  → Índigo/Violeta  : introspección, ciclos circadianos (Calm, 2024)
// GAD-7  → Azul océano     : reduce activación del SNS, frecuencia cardíaca
// PSS-10 → Verde sage      : recuperación, balance parasimpático
private val Phq9Accent   = Color(0xFF818CF8)   // indigo-400
private val Phq9Grad1    = Color(0xFF4F46E5)   // indigo-600
private val Gad7Accent   = Color(0xFF38BDF8)   // sky-400
private val Gad7Grad1    = Color(0xFF0369A1)   // sky-700
private val Pss10Accent  = Color(0xFF34D399)   // emerald-400
private val Pss10Grad1   = Color(0xFF047857)   // emerald-700

// ── Iconos SVG via drawBehind — sin emojis ────────────────────────────────────
// Cada test tiene un ícono dibujado con paths semánticamente coherentes

@Composable
fun HomeScreen(
    viewModel: MoodViewModel = viewModel(),
    onGoToGAD7:  () -> Unit = {},
    onGoToPSS10: () -> Unit = {},
    onGoToPHQ9:  () -> Unit = {},
    onGoToAudio: () -> Unit = {}
) {
    val userName        = viewModel.userName.split(" ").firstOrNull() ?: ""
    var selectedEmotion by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf(listOf<String>()) }
    val moods           = viewModel.moodHistory
    val gad7Results     = viewModel.gad7Results
    val pss10Results    = viewModel.pss10Results
    val phq9Results     = viewModel.phq9Results

    val hour     = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val greeting = when { hour < 12 -> "Buenos dias"; hour < 18 -> "Buenas tardes"; else -> "Buenas noches" }

    LaunchedEffect(Unit) { viewModel.loadAll() }

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 20.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(greeting, fontSize = 14.sp, color = TextWhite50)
                    Text(
                        if (userName.isNotEmpty()) "$userName!" else "Serenity!",
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextWhite
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AuroraStart, AuroraEnd))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        userName.firstOrNull()?.uppercaseChar()?.toString() ?: "S",
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp
                    )
                }
            }

            // ── Mood selector ─────────────────────────────────────────────
            Spacer(Modifier.height(28.dp))
            Text("Como te sientes hoy?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("😀" to "Feliz", "🙂" to "Bien", "😐" to "Neutral", "😔" to "Triste", "😰" to "Ansioso").forEach { (emoji, emotion) ->
                    DarkMoodCard(
                        emoji      = emoji,
                        label      = emotion,
                        isSelected = selectedEmotion == emotion,
                        onClick    = {
                            selectedEmotion = emotion
                            viewModel.registerMood(emotion)
                            recommendations = RecommendationEngine.getRecommendations(emotion)
                        }
                    )
                }
            }

            if (selectedEmotion.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text("Registraste: $selectedEmotion", fontSize = 13.sp, color = TealStart)
            }

            // ── Tests clínicos ────────────────────────────────────────────
            Spacer(Modifier.height(28.dp))
            Text("Cuestionarios clinicos", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(12.dp))

            // PHQ-9 — Depresión → Luna (ciclos, introspección)
            ClinicalTestCard(
                iconType   = TestIcon.MOON,
                accentColor = Phq9Accent,
                gradStart   = Phq9Grad1,
                title       = "Depresion - PHQ-9",
                subtitle    = if (phq9Results.isNotEmpty())
                    "Ultimo: ${phq9Results.last().score}/27 · ${phq9Results.last().severity}"
                else "Evalua tu estado de animo · 5 min",
                onClick     = onGoToPHQ9
            )
            Spacer(Modifier.height(10.dp))

            // GAD-7 — Ansiedad → Onda de respiración (regulación)
            ClinicalTestCard(
                iconType    = TestIcon.WAVE,
                accentColor = Gad7Accent,
                gradStart   = Gad7Grad1,
                title       = "Ansiedad - GAD-7",
                subtitle    = if (gad7Results.isNotEmpty())
                    "Ultimo: ${gad7Results.last().score}/21 · ${gad7Results.last().severity}"
                else "Evalua tu ansiedad · 3 min",
                onClick     = onGoToGAD7
            )
            Spacer(Modifier.height(10.dp))

            // PSS-10 — Estrés → Hoja (recuperación, naturaleza)
            ClinicalTestCard(
                iconType    = TestIcon.LEAF,
                accentColor = Pss10Accent,
                gradStart   = Pss10Grad1,
                title       = "Estres - PSS-10",
                subtitle    = if (pss10Results.isNotEmpty())
                    "Ultimo: ${pss10Results.last().score}/40 · ${pss10Results.last().severity}"
                else "Mide tu estres percibido · 5 min",
                onClick     = onGoToPSS10
            )
            Spacer(Modifier.height(10.dp))

            // NUEVA TARJETA: TERAPIA DE AUDIO
            ClinicalTestCard(
                iconType    = TestIcon.MOON, // Usamos la luna como ícono, o el que prefieras
                accentColor = TealStart,
                gradStart   = TealEnd,
                title       = "Terapia de Audio",
                subtitle    = "Sonidos para calmar la mente",
                onClick     = onGoToAudio // <-- ¡OJO AQUÍ! Ya no es un comentario, es la función
            )



            // ── Alerta preventiva ──────────────────────────────────────────
            val showAlert = gad7Results.size >= 2 && gad7Results.takeLast(2).all { it.score > 9 }
            if (showAlert) {
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AlertBackground)
                        .border(1.dp, AlertBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("⚠", fontSize = 18.sp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Alerta preventiva", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = SeveritySevere)
                            Text(
                                "Tu nivel de ansiedad ha sido elevado. Considera hablar con un profesional.",
                                fontSize = 12.sp, color = TextWhite50
                            )
                        }
                    }
                }
            }

            // ── Gráfica emocional ──────────────────────────────────────────
            if (moods.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text("Evolucion emocional", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                Text("Animo en el tiempo", fontSize = 12.sp, color = TextWhite30)
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceGlass)
                        .border(1.dp, BorderGlass, RoundedCornerShape(16.dp))
                ) {
                    EmotionChart(moods = moods)
                }
                Text(
                    EmotionAnalyzer.analyzeMood(moods),
                    color    = TextWhite70,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // ── Recomendaciones ────────────────────────────────────────────
            if (recommendations.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text("Recomendaciones para ti", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                Spacer(Modifier.height(10.dp))
                recommendations.forEach { rec ->
                    Row(
                        modifier          = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AuroraStart)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(rec, fontSize = 14.sp, color = TextWhite70)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

// ── Enum de íconos clínicos ───────────────────────────────────────────────────
enum class TestIcon { MOON, WAVE, LEAF }

// ── Card clínico con ícono SVG dibujado ──────────────────────────────────────
@Composable
fun ClinicalTestCard(
    iconType:    TestIcon,
    accentColor: Color,
    gradStart:   Color,
    title:       String,
    subtitle:    String,
    onClick:     () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        gradStart.copy(alpha = 0.35f),
                        accentColor.copy(alpha = 0.12f)
                    )
                )
            )
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono dibujado con paths
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            val emoji = when (iconType) {
                TestIcon.MOON -> "🌙"
                TestIcon.WAVE -> "〰️"
                TestIcon.LEAF -> "🌿"
            }
            Text(emoji, fontSize = 22.sp)
        }

        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title,    fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextWhite)
            Text(subtitle, fontSize   = 12.sp, color = TextWhite50, modifier = Modifier.padding(top = 3.dp))
        }
        // Chevron con el color del acento
        Text("›", fontSize = 24.sp, color = accentColor, fontWeight = FontWeight.Light)
    }
}

// ── Mood card — sin cambios ───────────────────────────────────────────────────
@Composable
fun DarkMoodCard(emoji: String, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected)
                    Brush.linearGradient(listOf(AuroraStart.copy(0.5f), AuroraEnd.copy(0.3f)))
                else
                    Brush.linearGradient(listOf(SurfaceGlass, SurfaceGlass))
            )
            .border(1.dp, if (isSelected) AuroraStart.copy(0.8f) else BorderGlass, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Text(emoji, fontSize = 22.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            fontSize   = 10.sp,
            color      = if (isSelected) TextWhite else TextWhite50,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// GlassTestCard se mantiene por compatibilidad si otros archivos la usan
@Composable
fun GlassTestCard(emoji: String, title: String, subtitle: String, gradientColors: List<Color>, accentColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(gradientColors))
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(accentColor.copy(0.2f)),
            contentAlignment = Alignment.Center
        ) { Text(emoji, fontSize = 24.sp) }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title,    fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextWhite)
            Text(subtitle, fontSize   = 12.sp, color = TextWhite50, modifier = Modifier.padding(top = 3.dp))
        }
        Text(">", fontSize = 22.sp, color = accentColor)
    }
}
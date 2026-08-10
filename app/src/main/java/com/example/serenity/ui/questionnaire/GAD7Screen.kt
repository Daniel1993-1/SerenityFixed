package com.example.serenity.ui.questionnaire

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

// ── Preguntas oficiales GAD-7 — Spitzer et al. (2006) ──────────────────────
private val gad7Questions = listOf(
    "¿Se ha sentido nervioso/a, ansioso/a o muy alterado/a?",
    "¿No ha podido dejar de preocuparse o controlar su preocupación?",
    "¿Se ha preocupado demasiado por diferentes cosas?",
    "¿Ha tenido dificultad para relajarse?",
    "¿Se ha sentido tan inquieto/a que no podía quedarse quieto/a?",
    "¿Se ha irritado o enojado con facilidad?",
    "¿Ha sentido miedo de que algo terrible pudiera pasar?"
)

private val gad7AnswerOptions = listOf(
    "Nunca" to 0,
    "Varios días" to 1,
    "Más de la mitad de los días" to 2,
    "Casi todos los días" to 3
)

// ── Data classes fuera del Composable para evitar recomposiciones ───────────
private data class ResultData(
    val severity: String,
    val color: Color,
    val description: String,
    val recommendation: String
)

private fun getGAD7Result(score: Int): ResultData = when {
    score <= 4  -> ResultData(
        "Ansiedad mínima", Color(0xFF4ECDC4),
        "Tu nivel de ansiedad es bajo. ¡Sigue así!",
        "Mantén tus hábitos saludables: ejercicio, sueño y conexión social."
    )
    score <= 9  -> ResultData(
        "Ansiedad leve", Color(0xFFFFD200),
        "Presentas síntomas leves de ansiedad.",
        "Practica técnicas de respiración y mindfulness. Si persiste, considera hablar con alguien de confianza."
    )
    score <= 14 -> ResultData(
        "Ansiedad moderada", Color(0xFFF7971E),
        "Tu nivel de ansiedad es moderado.",
        "⚠️ Se recomienda hablar con un profesional de salud mental. Considera buscar apoyo psicológico."
    )
    else        -> ResultData(
        "Ansiedad severa", Color(0xFFFF6B6B),
        "Presentas síntomas severos de ansiedad.",
        "🚨 Es importante que consultes con un profesional de salud mental lo antes posible. No estás solo/a."
    )
}

fun getSeverityLabel(score: Int): String = when {
    score <= 4  -> "minima"
    score <= 9  -> "leve"
    score <= 14 -> "moderada"
    else        -> "severa"
}

// ── Pantalla principal GAD-7 ────────────────────────────────────────────────
@Composable
fun GAD7Screen(onFinished: () -> Unit) {
    val auth  = FirebaseAuth.getInstance()
    val db    = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val answers = remember { mutableStateListOf(*Array(7) { -1 }) }
    var showResult   by remember { mutableStateOf(false) }
    var totalScore   by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }

    if (showResult) {
        GAD7ResultScreen(score = totalScore, onDone = onFinished)
        return
    }

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                "Cuestionario GAD-7",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Durante las últimas 2 semanas, ¿con qué frecuencia le han molestado los siguientes problemas?",
                fontSize = 14.sp,
                color = TextWhite50
            )
            Spacer(Modifier.height(24.dp))

            gad7Questions.forEachIndexed { index, question ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = SurfaceGlass
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "${index + 1}. $question",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                        Spacer(Modifier.height(12.dp))
                        gad7AnswerOptions.forEach { (label, value) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                RadioButton(
                                    selected = answers[index] == value,
                                    onClick  = { answers[index] = value },
                                    colors   = RadioButtonDefaults.colors(
                                        selectedColor   = AuroraStart,
                                        unselectedColor = TextWhite30
                                    )
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(label, fontSize = 14.sp, color = TextWhite70)
                            }
                        }
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = SeveritySevere, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (answers.any { it == -1 }) {
                        errorMessage = "Por favor responde todas las preguntas"
                        return@Button
                    }
                    val score = answers.sum()
                    totalScore = score
                    showResult = true
                    scope.launch {
                        try {
                            val uid = auth.currentUser?.uid ?: return@launch
                            db.collection("users").document(uid)
                                .collection("gad7_results")
                                .add(hashMapOf(
                                    "score"     to score,
                                    "answers"   to answers.toList(),
                                    "timestamp" to System.currentTimeMillis(),
                                    "severity"  to getSeverityLabel(score)
                                ))
                        } catch (e: Exception) {
                            android.util.Log.e("Serenity", "Error guardando GAD7: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AuroraStart)
            ) {
                Text("Ver resultado", fontSize = 16.sp, color = TextWhite)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Pantalla de resultado GAD-7 ─────────────────────────────────────────────
@Composable
fun GAD7ResultScreen(score: Int, onDone: () -> Unit) {
    val result = getGAD7Result(score)

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Text("Resultado GAD-7", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(Modifier.height(32.dp))

            // Puntaje circular
            Surface(
                modifier = Modifier.size(130.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = result.color.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(score.toString(), fontSize = 52.sp, fontWeight = FontWeight.Bold, color = result.color)
                        Text("/ 21", fontSize = 14.sp, color = TextWhite50)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(result.severity, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = result.color)
            Spacer(Modifier.height(16.dp))

            // Card descripción
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = SurfaceGlass
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(result.description, fontSize = 15.sp, color = TextWhite)
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = BorderGlass)
                    Spacer(Modifier.height(12.dp))
                    Text("Recomendación:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextWhite)
                    Spacer(Modifier.height(4.dp))
                    Text(result.recommendation, fontSize = 14.sp, color = TextWhite70)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Escala referencia
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = SurfaceGlass
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Escala de referencia GAD-7:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextWhite)
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("🟢 0–4",   fontSize = 12.sp, color = SeverityMinimal)
                        Text("🟡 5–9",   fontSize = 12.sp, color = SeverityMild)
                        Text("🟠 10–14", fontSize = 12.sp, color = SeverityModerate)
                        Text("🔴 15–21", fontSize = 12.sp, color = SeveritySevere)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Mínima",   fontSize = 11.sp, color = TextWhite50)
                        Text("Leve",     fontSize = 11.sp, color = TextWhite50)
                        Text("Moderada", fontSize = 11.sp, color = TextWhite50)
                        Text("Severa",   fontSize = 11.sp, color = TextWhite50)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AuroraStart)
            ) {
                Text("Volver al inicio", fontSize = 16.sp, color = TextWhite)
            }
        }
    }
}
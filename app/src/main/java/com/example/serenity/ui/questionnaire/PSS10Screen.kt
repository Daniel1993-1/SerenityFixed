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

// ── Preguntas oficiales PSS-10 — Cohen & Williamson (1988) / Remor (2006) ──
// Ítems invertidos (base-0): índices 3, 4, 6, 7
private val pssInvertedItems = setOf(3, 4, 6, 7)

private val pss10Questions = listOf(
    "¿Con qué frecuencia ha estado molesto/a por algo que ocurrió inesperadamente?",              // 1 — negativo
    "¿Con qué frecuencia se ha sentido incapaz de controlar las cosas importantes de su vida?",   // 2 — negativo
    "¿Con qué frecuencia se ha sentido nervioso/a y estresado/a?",                                 // 3 — negativo
    "¿Con qué frecuencia ha manejado con éxito los pequeños problemas irritantes de su vida?",    // 4 — positivo ✦
    "¿Con qué frecuencia ha sentido que estaba sobrellevando efectivamente los cambios importantes que ocurrían en su vida?", // 5 — positivo ✦
    "¿Con qué frecuencia ha estado seguro/a sobre su capacidad para manejar sus problemas personales?", // 6 — negativo
    "¿Con qué frecuencia ha sentido que las cosas le iban bien?",                                 // 7 — positivo ✦
    "¿Con qué frecuencia ha podido controlar las dificultades de su vida?",                       // 8 — positivo ✦
    "¿Con qué frecuencia ha sentido que tenía todo bajo control?",                                // 9 — negativo
    "¿Con qué frecuencia ha sentido que las dificultades se acumulaban tanto que no podía superarlas?" // 10 — negativo
)

private val pssAnswerOptions = listOf(
    "Nunca" to 0,
    "Casi nunca" to 1,
    "A veces" to 2,
    "Con bastante frecuencia" to 3,
    "Muy a menudo" to 4
)

// ── Data class fuera del Composable ─────────────────────────────────────────
private data class PSSResultData(
    val severity: String,
    val color: Color,
    val description: String,
    val recommendation: String
)

private fun getPSSResult(score: Int): PSSResultData = when {
    score <= 13 -> PSSResultData(
        "Estrés bajo", Color(0xFF4ECDC4),
        "Tu nivel de estrés percibido es bajo. Estás manejando bien las situaciones de tu vida.",
        "Sigue manteniendo tus hábitos: descanso adecuado, actividad física y tiempo para ti."
    )
    score <= 26 -> PSSResultData(
        "Estrés moderado", Color(0xFFF7971E),
        "Presentas un nivel moderado de estrés. Es algo común en el día a día.",
        "⚠️ Considera practicar técnicas de relajación, organizar mejor tus tiempos y hablar con alguien de confianza. Si persiste, busca apoyo profesional."
    )
    else        -> PSSResultData(
        "Estrés alto", Color(0xFFFF6B6B),
        "Tu nivel de estrés es alto. Esto puede afectar tu salud y bienestar general.",
        "🚨 Se recomienda buscar apoyo con un psicólogo o consejero. No tienes que manejarlo solo/a."
    )
}

fun getPSSSeverityLabel(score: Int): String = when {
    score <= 13 -> "bajo"
    score <= 26 -> "moderado"
    else        -> "alto"
}

// ── Pantalla principal PSS-10 ────────────────────────────────────────────────
@Composable
fun PSS10Screen(onFinished: () -> Unit) {
    val auth  = FirebaseAuth.getInstance()
    val db    = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val answers = remember { mutableStateListOf(*Array(10) { -1 }) }
    var showResult   by remember { mutableStateOf(false) }
    var totalScore   by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }

    if (showResult) {
        PSSResultScreen(score = totalScore, onDone = onFinished)
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
                "Cuestionario PSS-10",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Escala de Estrés Percibido — En el último mes, ¿con qué frecuencia...?",
                fontSize = 14.sp,
                color = TextWhite50
            )
            Spacer(Modifier.height(24.dp))

            pss10Questions.forEachIndexed { index, question ->
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
                        pssAnswerOptions.forEach { (label, value) ->
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
                                        selectedColor   = TealStart,
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
                    // Calcular puntaje PSS-10 con inversión correcta (índices 3,4,6,7)
                    val score = answers.mapIndexed { index, value ->
                        if (index in pssInvertedItems) 4 - value else value
                    }.sum()
                    totalScore = score
                    showResult = true
                    scope.launch {
                        try {
                            val uid = auth.currentUser?.uid ?: return@launch
                            db.collection("users").document(uid)
                                .collection("pss10_results")
                                .add(hashMapOf(
                                    "score"     to score,
                                    "answers"   to answers.toList(),
                                    "timestamp" to System.currentTimeMillis(),
                                    "severity"  to getPSSSeverityLabel(score)
                                ))
                        } catch (e: Exception) {
                            android.util.Log.e("Serenity", "Error guardando PSS10: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealStart)
            ) {
                Text("Ver resultado", fontSize = 16.sp, color = TextWhite)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Pantalla de resultado PSS-10 ─────────────────────────────────────────────
@Composable
fun PSSResultScreen(score: Int, onDone: () -> Unit) {
    val result = getPSSResult(score)

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Text("Resultado PSS-10", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
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
                        Text("/ 40", fontSize = 14.sp, color = TextWhite50)
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
                    Text("Escala de referencia PSS-10:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextWhite)
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("🟢 0–13",  fontSize = 12.sp, color = SeverityMinimal)
                        Text("🟠 14–26", fontSize = 12.sp, color = SeverityModerate)
                        Text("🔴 27–40", fontSize = 12.sp, color = SeveritySevere)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Bajo",     fontSize = 11.sp, color = TextWhite50)
                        Text("Moderado", fontSize = 11.sp, color = TextWhite50)
                        Text("Alto",     fontSize = 11.sp, color = TextWhite50)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealStart)
            ) {
                Text("Volver al inicio", fontSize = 16.sp, color = TextWhite)
            }
        }
    }
}
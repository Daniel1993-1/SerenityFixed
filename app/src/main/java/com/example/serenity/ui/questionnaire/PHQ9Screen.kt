package com.example.serenity.ui.questionnaire

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

private val phq9Questions = listOf(
    "Poco interes o placer en hacer las cosas",
    "Sentirse desanimado/a, deprimido/a o sin esperanza",
    "Dificultad para dormir, mantenerse dormido/a o dormir demasiado",
    "Sentirse cansado/a o con poca energia",
    "Tener poco apetito o comer en exceso",
    "Sentirse mal con usted mismo/a, sentir que es un fracaso o que se ha fallado a si mismo/a o a su familia",
    "Dificultad para concentrarse en cosas como leer el periodico o ver television",
    "Moverse o hablar tan lento que otras personas lo notaron, o lo contrario: estar tan agitado/a que se movia mas de lo normal",
    "Pensamientos de que seria mejor estar muerto/a o de hacerse dano de alguna manera"
)

private val phq9Options = listOf(
    "Nunca" to 0,
    "Varios dias" to 1,
    "Mas de la mitad de los dias" to 2,
    "Casi todos los dias" to 3
)

private data class PHQ9ResultData(val severity: String, val color: Color, val description: String, val recommendation: String)

private fun getPHQ9Result(score: Int): PHQ9ResultData = when {
    score <= 4  -> PHQ9ResultData("Sin depresion", Color(0xFF4ECDC4),
        "No se detectan sintomas significativos de depresion.",
        "Mantien tus habitos saludables: sueno regular, actividad fisica y conexion social.")
    score <= 9  -> PHQ9ResultData("Depresion leve", Color(0xFFFFD200),
        "Presentas sintomas leves de depresion.",
        "Considera hablar con alguien de confianza y mantener rutinas. Si persiste mas de 2 semanas, consulta un profesional.")
    score <= 14 -> PHQ9ResultData("Depresion moderada", Color(0xFFF7971E),
        "Tus sintomas indican un nivel moderado de depresion.",
        "Se recomienda buscar apoyo profesional (psicologo o medico). No esperes a que los sintomas empeoren.")
    score <= 19 -> PHQ9ResultData("Depresion moderada-severa", Color(0xFFFF9F43),
        "Los sintomas son moderados a severos y pueden estar afectando tu vida diaria.",
        "Es importante que consultes con un profesional de salud mental lo antes posible.")
    else        -> PHQ9ResultData("Depresion severa", Color(0xFFFF6B6B),
        "Presentas sintomas severos de depresion que requieren atencion profesional urgente.",
        "Por favor busca ayuda profesional de inmediato. Llama a la Linea 106 si necesitas apoyo ahora mismo. No estas solo/a.")
}

fun getPHQ9SeverityLabel(score: Int): String = when {
    score <= 4  -> "minima"
    score <= 9  -> "leve"
    score <= 14 -> "moderada"
    score <= 19 -> "moderada_severa"
    else        -> "severa"
}

@Composable
fun PHQ9Screen(onFinished: () -> Unit) {
    val auth  = FirebaseAuth.getInstance()
    val db    = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val answers = remember { mutableStateListOf(*Array(9) { -1 }) }
    var showResult      by remember { mutableStateOf(false) }
    var showCrisisAlert by remember { mutableStateOf(false) }
    var totalScore      by remember { mutableStateOf(0) }
    var errorMessage    by remember { mutableStateOf("") }

    if (showCrisisAlert) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = Color(0xFF2A0A0A),
            icon  = { Text("SOS", fontSize = 28.sp, color = SeveritySevere, fontWeight = FontWeight.Bold) },
            title = { Text("Necesitas apoyo ahora", fontWeight = FontWeight.Bold, color = SeveritySevere, fontSize = 18.sp) },
            text  = {
                Column {
                    Text(
                        "Has indicado tener pensamientos de hacerte dano. Tu vida tiene valor y hay personas que pueden ayudarte.",
                        fontSize = 14.sp, color = TextWhite70, lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SeveritySevere.copy(alpha = 0.15f))
                            .border(1.dp, SeveritySevere.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("Linea 106 - Colombia", fontWeight = FontWeight.Bold, color = SeveritySevere, fontSize = 14.sp)
                            Text("Salud mental - Disponible 24 horas", fontSize = 12.sp, color = TextWhite50)
                            Spacer(Modifier.height(6.dp))
                            Text("Linea 123 - Emergencias", fontWeight = FontWeight.Bold, color = SeverityModerate, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showCrisisAlert = false; showResult = true },
                    colors  = ButtonDefaults.buttonColors(containerColor = AuroraStart),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Entendido, ver mi resultado", color = TextWhite) }
            }
        )
    }

    if (showResult) { PHQ9ResultScreen(score = totalScore, onDone = onFinished); return }

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Cuestionario PHQ-9", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(Modifier.height(4.dp))
            Text("Durante las ultimas 2 semanas, con que frecuencia le han molestado los siguientes problemas?",
                fontSize = 14.sp, color = TextWhite50)
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AuroraStart.copy(alpha = 0.1f))
                    .border(1.dp, AuroraStart.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text("Esta herramienta es de automonitoreo, no reemplaza el diagnostico clinico. Consulta siempre con un profesional de salud.",
                    fontSize = 12.sp, color = TextWhite50, lineHeight = 18.sp)
            }

            Spacer(Modifier.height(20.dp))

            phq9Questions.forEachIndexed { index, question ->
                val isCrisis = index == 8
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape    = MaterialTheme.shapes.medium,
                    color    = if (isCrisis) SeveritySevere.copy(alpha = 0.08f) else SurfaceGlass
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (isCrisis) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SeveritySevere.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) { Text("Pregunta de bienestar", fontSize = 10.sp, color = SeveritySevere, fontWeight = FontWeight.SemiBold) }
                            Spacer(Modifier.height(8.dp))
                        }
                        Text("${index + 1}. $question", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextWhite, lineHeight = 22.sp)
                        Spacer(Modifier.height(12.dp))
                        phq9Options.forEach { (label, value) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            ) {
                                RadioButton(
                                    selected = answers[index] == value,
                                    onClick  = { answers[index] = value },
                                    colors   = RadioButtonDefaults.colors(
                                        selectedColor   = if (isCrisis) SeveritySevere else LavenderEnd,
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

            if (errorMessage.isNotEmpty()) { Text(errorMessage, color = SeveritySevere, fontSize = 14.sp); Spacer(Modifier.height(8.dp)) }

            Button(
                onClick = {
                    if (answers.any { it == -1 }) { errorMessage = "Por favor responde todas las preguntas"; return@Button }
                    val score = answers.sum()
                    totalScore = score
                    scope.launch {
                        try {
                            val uid = auth.currentUser?.uid ?: return@launch
                            db.collection("users").document(uid).collection("phq9_results").add(hashMapOf(
                                "score"     to score,
                                "answers"   to answers.toList(),
                                "timestamp" to System.currentTimeMillis(),
                                "severity"  to getPHQ9SeverityLabel(score)
                            ))
                        } catch (e: Exception) { android.util.Log.e("Serenity", "Error PHQ9: ${e.message}") }
                    }
                    if (answers[8] > 0) showCrisisAlert = true else showResult = true
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = LavenderEnd)
            ) { Text("Ver resultado", fontSize = 16.sp, color = TextWhite) }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun PHQ9ResultScreen(score: Int, onDone: () -> Unit) {
    val result = getPHQ9Result(score)
    SerenityBackground {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Text("Resultado PHQ-9", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(Modifier.height(32.dp))
            Surface(modifier = Modifier.size(130.dp), shape = MaterialTheme.shapes.extraLarge, color = result.color.copy(alpha = 0.2f)) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(score.toString(), fontSize = 52.sp, fontWeight = FontWeight.Bold, color = result.color)
                        Text("/ 27", fontSize = 14.sp, color = TextWhite50)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(result.severity, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = result.color)
            Spacer(Modifier.height(16.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large, color = SurfaceGlass) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(result.description, fontSize = 15.sp, color = TextWhite, lineHeight = 22.sp)
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = BorderGlass)
                    Spacer(Modifier.height(12.dp))
                    Text("Recomendacion:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextWhite)
                    Spacer(Modifier.height(4.dp))
                    Text(result.recommendation, fontSize = 14.sp, color = TextWhite70, lineHeight = 22.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large, color = SurfaceGlass) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Escala PHQ-9:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextWhite)
                    Spacer(Modifier.height(10.dp))
                    listOf(
                        Triple("0-4",   "Sin depresion",     SeverityMinimal),
                        Triple("5-9",   "Leve",              SeverityMild),
                        Triple("10-14", "Moderada",          SeverityModerate),
                        Triple("15-19", "Moderada-severa",   Color(0xFFFF9F43)),
                        Triple("20-27", "Severa",            SeveritySevere)
                    ).forEach { (range, label, color) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(range, fontSize = 12.sp, color = color)
                            Text(label, fontSize = 12.sp, color = TextWhite50)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(AuroraStart.copy(alpha = 0.2f), TealStart.copy(alpha = 0.1f))))
                    .border(1.dp, AuroraStart.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Necesitas hablar con alguien?", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextWhite)
                    Spacer(Modifier.height(8.dp))
                    Text("Linea 106 - Salud mental - 24h - Colombia", fontSize = 13.sp, color = TextWhite70)
                    Text("Linea 123 - Emergencias", fontSize = 13.sp, color = TextWhite70)
                }
            }
            Spacer(Modifier.height(32.dp))
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LavenderEnd)) {
                Text("Volver al inicio", fontSize = 16.sp, color = TextWhite)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}


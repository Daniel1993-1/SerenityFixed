package com.example.serenity.ui.audio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenity.ui.AudioViewModel
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*

@Composable
fun AudioTherapyScreen(
    viewModel: AudioViewModel = viewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text("Terapia de Audio", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("Sonidos basados en evidencia científica", fontSize = 14.sp, color = TextWhite50)

            Spacer(Modifier.height(20.dp))

            // Tarjeta 1: Alpha
            AudioCard(
                title = "Calma Profunda (Alpha 10 Hz)",
                subtitle = "Reduce ansiedad y estrés. (Requiere auriculares)",
                color = TealStart,
                isPlaying = viewModel.isPlaying && viewModel.currentType == "ALPHA",
                onClick = { viewModel.toggleAudio(context, "ALPHA") }
            )

            Spacer(Modifier.height(12.dp))

            // Tarjeta 2: Theta
            AudioCard(
                title = "Relajación (Theta 6 Hz)",
                subtitle = "Ideal para meditación y sueño. (Requiere auriculares)",
                color = LavenderEnd,
                isPlaying = viewModel.isPlaying && viewModel.currentType == "THETA",
                onClick = { viewModel.toggleAudio(context, "THETA") }
            )

            Spacer(Modifier.height(12.dp))

            // Tarjeta 3: 528 Hz
            AudioCard(
                title = "Pausa 528 Hz (Solfeggio)",
                subtitle = "Reduce el cortisol y la tensión.",
                color = AuroraStart,
                isPlaying = viewModel.isPlaying && viewModel.currentType == "528HZ",
                onClick = { viewModel.toggleAudio(context, "528HZ") }
            )

            Spacer(Modifier.height(24.dp))

            // Botón para detener todo
            if (viewModel.isPlaying) {
                Button(
                    onClick = { viewModel.stopAudio() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SeveritySevere)
                ) {
                    Text("Detener Audio", color = Color.White)
                }
            }

            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al Inicio", color = TextWhite50)
            }
        }
    }
}

@Composable
fun AudioCard(
    title: String,
    subtitle: String,
    color: Color,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceGlass)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador visual de Play/Pause
            Text(
                text = if (isPlaying) "⏸" else "▶",
                fontSize = 24.sp,
                color = color
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(subtitle, fontSize = 12.sp, color = TextWhite50)
            }
        }
    }
}
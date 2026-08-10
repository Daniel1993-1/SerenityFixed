package com.example.serenity.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenity.ui.MoodViewModel
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    viewModel: MoodViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    val auth         = FirebaseAuth.getInstance()
    val userName     = viewModel.userName
    val userEmail    = viewModel.userEmail
    val moodCount    = viewModel.moodHistory.size
    val phq9Count    = viewModel.phq9Results.size
    val gad7Count    = viewModel.gad7Results.size
    val pss10Count   = viewModel.pss10Results.size

    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadAll() }

    // Dialogo de confirmacion logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor   = Color(0xFF1C1B1F),
            title = { Text("Cerrar sesion", fontWeight = FontWeight.Bold, color = TextWhite) },
            text  = { Text("Estas seguro/a que deseas cerrar sesion?", color = TextWhite70) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.clearOnLogout()
                        auth.signOut()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SeveritySevere)
                ) { Text("Cerrar sesion", color = TextWhite) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = TextWhite50)
                }
            }
        )
    }

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 24.dp)
        ) {
            Text("Perfil", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(Modifier.height(24.dp))

            // Avatar + nombre + email
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(AuroraStart.copy(alpha = 0.3f), TealStart.copy(alpha = 0.15f)))
                    )
                    .border(1.dp, AuroraStart.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AuroraStart, AuroraEnd))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                            fontSize   = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text       = if (userName.isNotEmpty()) userName else "Usuario",
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color      = TextWhite
                        )
                        Text(
                            text     = userEmail,
                            fontSize = 13.sp,
                            color    = TextWhite50
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Estadisticas
            Text("Mis estadisticas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(Modifier.weight(1f), moodCount.toString(),  "Registros\nanimo",   AuroraStart)
                StatCard(Modifier.weight(1f), phq9Count.toString(),  "Tests\nPHQ-9",       LavenderEnd)
                StatCard(Modifier.weight(1f), (gad7Count + pss10Count).toString(), "Tests\nGAD/PSS", TealStart)
            }

            // Ultimo resultado PHQ-9
            if (viewModel.phq9Results.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))
                val last  = viewModel.phq9Results.last()
                val color = when (last.severity) {
                    "minima"          -> SeverityMinimal
                    "leve"            -> SeverityMild
                    "moderada"        -> SeverityModerate
                    "moderada_severa" -> Color(0xFFFF9F43)
                    else              -> SeveritySevere
                }
                Text("Ultimo resultado PHQ-9", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                Spacer(Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    color    = SurfaceGlass
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Puntaje: ${last.score}/27", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextWhite)
                            Text("Depresion", fontSize = 12.sp, color = TextWhite50)
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = color.copy(alpha = 0.15f)) {
                            Text(
                                last.severity.replace("_", " "),
                                fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Ultimo resultado GAD-7
            if (viewModel.gad7Results.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                val last  = viewModel.gad7Results.last()
                val color = when (last.severity) {
                    "minima"   -> SeverityMinimal
                    "leve"     -> SeverityMild
                    "moderada" -> SeverityModerate
                    else       -> SeveritySevere
                }
                Text("Ultimo resultado GAD-7", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                Spacer(Modifier.height(10.dp))
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = SurfaceGlass) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Puntaje: ${last.score}/21", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextWhite)
                            Text("Ansiedad", fontSize = 12.sp, color = TextWhite50)
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = color.copy(alpha = 0.15f)) {
                            Text(last.severity, fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp))
                        }
                    }
                }
            }

            // Ultimo resultado PSS-10
            if (viewModel.pss10Results.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                val last  = viewModel.pss10Results.last()
                val color = when (last.severity) {
                    "bajo"     -> SeverityMinimal
                    "moderado" -> SeverityModerate
                    else       -> SeveritySevere
                }
                Text("Ultimo resultado PSS-10", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                Spacer(Modifier.height(10.dp))
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = SurfaceGlass) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Puntaje: ${last.score}/40", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextWhite)
                            Text("Estres", fontSize = 12.sp, color = TextWhite50)
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = color.copy(alpha = 0.15f)) {
                            Text(last.severity, fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Opciones de menu
            Text("Configuracion", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(12.dp))

            ProfileMenuItem(icon = Icons.Outlined.Notifications, label = "Recordatorios", subtitle = "Configura alertas de seguimiento")
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(icon = Icons.Outlined.Shield, label = "Privacidad", subtitle = "Tus datos son privados y seguros")
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(icon = Icons.Outlined.Info, label = "Acerca de Serenity", subtitle = "Version 1.0 - Herramienta de automonitoreo")

            Spacer(Modifier.height(28.dp))

            // Aviso legal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AuroraStart.copy(alpha = 0.08f))
                    .border(1.dp, AuroraStart.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    "Serenity es una herramienta de automonitoreo y psicoeducacion. No reemplaza la consulta con un profesional de salud mental. Si necesitas ayuda: Linea 106 (Colombia) - 24 horas.",
                    fontSize   = 11.sp,
                    color      = TextWhite50,
                    lineHeight = 17.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            // Boton cerrar sesion
            OutlinedButton(
                onClick  = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = SeveritySevere),
                border   = androidx.compose.foundation.BorderStroke(1.dp, SeveritySevere.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = null, tint = SeveritySevere)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesion", fontSize = 16.sp, color = SeveritySevere, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, value: String, label: String, color: Color) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = SurfaceGlass, shadowElevation = 0.dp) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = TextWhite50, lineHeight = 15.sp)
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, subtitle: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = SurfaceGlass, shadowElevation = 0.dp) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(AuroraStart.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = AuroraStart, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label,    fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextWhite)
                Text(subtitle, fontSize = 11.sp, color = TextWhite50)
            }
            Text(">", fontSize = 16.sp, color = TextWhite30)
        }
    }
}
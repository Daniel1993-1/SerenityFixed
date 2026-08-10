package com.example.serenity.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenity.ui.components.ScreenBackground
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.components.SerenityLogo
import com.example.serenity.ui.theme.CtaPrimaryEnd
import com.example.serenity.ui.theme.CtaPrimaryStart

private val TealAccent  = Color(0xFF2DD4BF)
private val CardBg      = Color(0xFF0D1F2D)
private val TextPrimary = Color(0xFFF1F5F9)
private val TextMuted   = Color(0xFF8BA8BC)
private val DividerCol  = Color(0xFF1E3448)
private val ErrorRed    = Color(0xFFFF6B6B)

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onGoToLogin:       () -> Unit = {},
    viewModel: RegisterViewModel   = viewModel()
) {
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirm         by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }

    // ── Consentimiento Ley 1581 ───────────────────────────────────────────────
    var consentAccepted  by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val isLoading    = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    // ── Modal política de privacidad ──────────────────────────────────────────
    if (showPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
    }

    SerenityBackground(screen = ScreenBackground.LOGIN) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(44.dp))

            SerenityLogo(iconSize = 72.dp, showTagline = false)

            Spacer(Modifier.height(24.dp))

            Text(
                text       = "Crear cuenta",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text      = "Únete a tu espacio seguro de bienestar",
                fontSize  = 13.sp,
                color     = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // ── Campos ────────────────────────────────────────────────────────
            RegisterField(name,  { name  = it; viewModel.clearError() }, "Nombre completo",    KeyboardType.Text)
            Spacer(Modifier.height(14.dp))
            RegisterField(email, { email = it; viewModel.clearError() }, "Correo electrónico", KeyboardType.Email)
            Spacer(Modifier.height(14.dp))
            RegisterField(
                value = password, onValueChange = { password = it; viewModel.clearError() },
                placeholder = "Contraseña", isPassword = true,
                passwordVisible = passwordVisible, onTogglePassword = { passwordVisible = !passwordVisible }
            )
            Spacer(Modifier.height(14.dp))
            RegisterField(
                value = confirm, onValueChange = { confirm = it; viewModel.clearError() },
                placeholder = "Confirmar contraseña", isPassword = true,
                passwordVisible = confirmVisible, onTogglePassword = { confirmVisible = !confirmVisible }
            )

            Spacer(Modifier.height(20.dp))

            // ── Checkbox consentimiento Ley 1581 ──────────────────────────────
            Row(
                verticalAlignment = Alignment.Top,
                modifier          = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked         = consentAccepted,
                    onCheckedChange = { consentAccepted = it },
                    colors          = CheckboxDefaults.colors(
                        checkedColor   = TealAccent,
                        uncheckedColor = TextMuted
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Acepto el tratamiento de mis datos de salud conforme a la ")
                        withStyle(SpanStyle(color = TealAccent, fontWeight = FontWeight.SemiBold)) {
                            append("Política de Privacidad")
                        }
                        append(" (Ley 1581/2012). Entiendo que esta app es una herramienta de apoyo y ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = TextPrimary)) {
                            append("no reemplaza atención médica profesional.")
                        }
                    },
                    fontSize   = 12.sp,
                    color      = TextMuted,
                    lineHeight = 17.sp,
                    modifier   = Modifier
                        .clickable { showPrivacyDialog = true }
                        .padding(top = 2.dp)
                )
            }

            // ── Error ─────────────────────────────────────────────────────────
            if (errorMessage != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text      = errorMessage!!,
                    color     = ErrorRed,
                    fontSize  = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth()
                )
            }

            // ── Advertencia si no acepta consentimiento ───────────────────────
            if (!consentAccepted) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text      = "Debes aceptar el tratamiento de datos para continuar",
                    color     = ErrorRed.copy(alpha = 0.7f),
                    fontSize  = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Botón crear cuenta ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        when {
                            !consentAccepted || isLoading ->
                                Brush.horizontalGradient(listOf(Color(0xFF2A7A76), Color(0xFF22605C)))
                            else ->
                                Brush.horizontalGradient(listOf(CtaPrimaryStart, CtaPrimaryEnd))
                        }
                    )
                    .clickable(
                        enabled           = consentAccepted && !isLoading,
                        indication        = ripple(),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.register(name, email, password, confirm, onSuccess = onRegisterSuccess)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color       = Color(0xFF071622),
                        modifier    = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text       = "Crear cuenta",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = if (consentAccepted) Color(0xFF071622) else Color(0xFF071622).copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            TextButton(onClick = { onGoToLogin() }) {
                Text(
                    text     = "¿Ya tienes cuenta? Inicia sesión",
                    color    = TealAccent,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Modal Política de Privacidad (Ley 1581/2012) ──────────────────────────────
@Composable
private fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape             = RoundedCornerShape(20.dp),
            color             = Color(0xFF0D1F2D),
            tonalElevation    = 8.dp,
            modifier          = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text       = "Política de Privacidad",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text     = "Cumplimiento Ley 1581 de 2012 — Colombia",
                    fontSize = 11.sp,
                    color    = TealAccent
                )

                Spacer(Modifier.height(16.dp))

                PolicySection(
                    title = "¿Qué datos recopilamos?",
                    body  = "Nombre, correo electrónico, respuestas a cuestionarios psicológicos validados (PHQ-9, GAD-7, PSS-10) y registros de estado de ánimo."
                )
                PolicySection(
                    title = "¿Para qué los usamos?",
                    body  = "Exclusivamente para ofrecerte seguimiento personalizado de tu bienestar emocional. No compartimos ni vendemos tus datos a terceros."
                )
                PolicySection(
                    title = "¿Dónde se almacenan?",
                    body  = "En Google Firebase (servidores con certificación ISO 27001). Tus datos están cifrados en tránsito y en reposo."
                )
                PolicySection(
                    title = "Datos sensibles de salud",
                    body  = "Los resultados de los cuestionarios son datos sensibles según el Art. 5 de la Ley 1581/2012. Solo tú tienes acceso a ellos."
                )
                PolicySection(
                    title = "Tus derechos",
                    body  = "Puedes conocer, actualizar, rectificar y suprimir tus datos en cualquier momento desde la sección Perfil de la app."
                )
                PolicySection(
                    title = "⚠️ Aviso clínico importante",
                    body  = "Serenity es una herramienta de apoyo al bienestar. Los resultados de los cuestionarios son orientativos y NO constituyen un diagnóstico clínico. Ante cualquier crisis de salud mental, consulta un profesional de salud."
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick  = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = TealAccent,
                        contentColor   = Color(0xFF071622)
                    )
                ) {
                    Text("Entendido", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PolicySection(title: String, body: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(Modifier.height(3.dp))
        Text(text = body,  fontSize = 12.sp, color = TextMuted, lineHeight = 18.sp)
    }
}

@Composable
private fun RegisterField(
    value:            String,
    onValueChange:    (String) -> Unit,
    placeholder:      String,
    keyboardType:     KeyboardType = KeyboardType.Text,
    isPassword:       Boolean      = false,
    passwordVisible:  Boolean      = false,
    onTogglePassword: () -> Unit   = {}
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        modifier      = Modifier.fillMaxWidth(),
        placeholder   = { Text(placeholder, color = TextMuted, fontSize = 14.sp) },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon    = if (isPassword) {
            {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        if (passwordVisible) Icons.Outlined.Visibility
                        else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint               = TextMuted
                    )
                }
            }
        } else null,
        shape  = RoundedCornerShape(13.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = TealAccent,
            unfocusedBorderColor    = DividerCol,
            focusedTextColor        = TextPrimary,
            unfocusedTextColor      = TextPrimary,
            cursorColor             = TealAccent,
            focusedContainerColor   = CardBg,
            unfocusedContainerColor = CardBg
        ),
        singleLine = true
    )
}
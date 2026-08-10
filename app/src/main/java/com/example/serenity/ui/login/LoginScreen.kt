package com.example.serenity.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serenity.ui.components.ScreenBackground
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.components.SerenityLogo
import com.example.serenity.ui.theme.CtaPrimaryEnd
import com.example.serenity.ui.theme.CtaPrimaryStart

private val CardBg      = Color(0xFF0D1F2D)
private val TextPrimary = Color(0xFFF1F5F9)
private val TextMuted   = Color(0xFF8BA8BC)
private val DividerCol  = Color(0xFF1E3448)
private val TealAccent  = Color(0xFF2DD4BF)
private val ErrorRed    = Color(0xFFFF6B6B)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onGoToRegister: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    SerenityBackground(screen = ScreenBackground.LOGIN) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            SerenityLogo(iconSize = 104.dp, showTagline = true)

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Tu espacio seguro de bienestar",
                fontSize = 13.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            SerenityTextField(
                value = email,
                onValueChange = { email = it; viewModel.clearError() },
                placeholder = "Correo electrónico",
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(14.dp))

            SerenityTextField(
                value = password,
                onValueChange = { password = it; viewModel.clearError() },
                placeholder = "Contraseña",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            if (errorMessage != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = ErrorRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isLoading) {
                            Brush.horizontalGradient(
                                listOf(Color(0xFF2A7A76), Color(0xFF22605C))
                            )
                        } else {
                            Brush.horizontalGradient(
                                listOf(CtaPrimaryStart, CtaPrimaryEnd)
                            )
                        }
                    )
                    .clickable(
                        enabled = !isLoading,
                        indication = ripple(),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.login(email, password, onSuccess = onLoginSuccess)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF071622),
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF071622)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = DividerCol)
                Text(text = "  o  ", color = TextMuted, fontSize = 13.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = DividerCol)
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = { onGoToRegister() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DividerCol),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
            ) {
                Text("Crear cuenta nueva", fontSize = 15.sp)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Al continuar aceptas que esta app es una herramienta de apoyo, no un sustituto médico.",
                fontSize = 11.sp,
                color = TextMuted.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SerenityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = TextMuted, fontSize = 14.sp) },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                        tint = TextMuted
                    )
                }
            }
        } else null,
        shape = RoundedCornerShape(13.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TealAccent,
            unfocusedBorderColor = DividerCol,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = TealAccent,
            focusedContainerColor = CardBg,
            unfocusedContainerColor = CardBg
        ),
        singleLine = true
    )
}
package com.example.serenity.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank()) {
            errorMessage = "Ingresa tu correo electrónico"
            return
        }
        if (password.isBlank()) {
            errorMessage = "Ingresa tu contraseña"
            return
        }
        if (password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = when {
                    e.message?.contains("no user record") == true ||
                            e.message?.contains("user-not-found") == true ->
                        "No existe una cuenta con ese correo"

                    e.message?.contains("password is invalid") == true ||
                            e.message?.contains("wrong-password") == true ->
                        "Contraseña incorrecta"

                    e.message?.contains("badly formatted") == true ->
                        "El formato del correo no es válido"

                    e.message?.contains("network") == true ||
                            e.message?.contains("NETWORK") == true ->
                        "Sin conexión a internet. Verifica tu red."

                    e.message?.contains("too-many-requests") == true ->
                        "Demasiados intentos. Espera unos minutos."

                    else -> "Error al iniciar sesión. Intenta de nuevo."
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
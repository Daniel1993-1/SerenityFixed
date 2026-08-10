package com.example.serenity.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    var isLoading    by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(
        name:      String,
        email:     String,
        password:  String,
        confirm:   String,
        onSuccess: () -> Unit
    ) {
        // ── Validaciones ────────────────────────────────────────────────────
        if (name.isBlank()) {
            errorMessage = "Ingresa tu nombre completo"; return
        }
        if (email.isBlank()) {
            errorMessage = "Ingresa tu correo electrónico"; return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "El formato del correo no es válido"; return
        }
        if (password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"; return
        }
        if (password != confirm) {
            errorMessage = "Las contraseñas no coinciden"; return
        }

        isLoading    = true
        errorMessage = null

        viewModelScope.launch {
            try {
                // 1. Crear usuario en Firebase Auth
                val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
                val uid    = result.user?.uid ?: throw Exception("No se obtuvo UID")

                // 2. Guardar nombre en Firestore
                db.collection("users").document(uid).set(
                    mapOf(
                        "name"      to name.trim(),
                        "email"     to email.trim(),
                        "createdAt" to System.currentTimeMillis(),
                        "consentAccepted" to true
                    )
                ).await()

                onSuccess()
            } catch (e: Exception) {
                errorMessage = when {
                    e.message?.contains("email address is already") == true ||
                            e.message?.contains("email-already-in-use")    == true ->
                        "Ya existe una cuenta con ese correo"
                    e.message?.contains("badly formatted")         == true ->
                        "El formato del correo no es válido"
                    e.message?.contains("network")                 == true ||
                            e.message?.contains("NETWORK")                 == true ->
                        "Sin conexión a internet. Verifica tu red."
                    e.message?.contains("weak-password")           == true ->
                        "Contraseña muy débil. Usa al menos 6 caracteres."
                    else -> "Error al crear la cuenta. Intenta de nuevo."
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() { errorMessage = null }
}
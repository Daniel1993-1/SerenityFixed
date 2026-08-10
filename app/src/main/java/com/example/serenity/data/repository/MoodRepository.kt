package com.example.serenity.data.repository

import com.example.serenity.data.model.Mood
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object MoodRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Guardar un mood en Firestore
    fun addMood(emotion: String, onComplete: (Boolean) -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return

        val moodData = hashMapOf(
            "emotion" to emotion,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(uid)
            .collection("moods")
            .add(moodData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Obtener historial de moods desde Firestore
    fun getMoods(onResult: (List<Mood>) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("moods")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val moods = result.documents.mapNotNull { doc ->
                    val emotion = doc.getString("emotion") ?: return@mapNotNull null
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                    Mood(emotion, timestamp)
                }
                onResult(moods)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}
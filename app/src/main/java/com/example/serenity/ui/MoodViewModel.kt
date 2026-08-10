package com.example.serenity.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serenity.data.model.Mood
import com.example.serenity.data.model.TestResult
import com.example.serenity.data.repository.MoodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MoodViewModel : ViewModel() {
    val moodHistory  = mutableStateListOf<Mood>()
    val gad7Results  = mutableStateListOf<TestResult>()
    val pss10Results = mutableStateListOf<TestResult>()
    val phq9Results  = mutableStateListOf<TestResult>()

    var userName by mutableStateOf("")
        private set

    val userEmail: String get() = FirebaseAuth.getInstance().currentUser?.email ?: ""

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()
    private var loaded = false

    fun loadAll(forceReload: Boolean = false) {
        if (loaded && !forceReload) return
        loaded = true
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val doc = db.collection("users").document(uid).get().await()
                userName = doc.getString("name") ?: ""
            } catch (e: Exception) { android.util.Log.e("Serenity", "Error nombre: ${e.message}") }
        }

        MoodRepository.getMoods { moods -> moodHistory.clear(); moodHistory.addAll(moods) }

        viewModelScope.launch {
            try {
                val snap = db.collection("users").document(uid).collection("gad7_results").orderBy("timestamp").get().await()
                gad7Results.clear()
                gad7Results.addAll(snap.documents.mapNotNull {
                    TestResult((it.getLong("score") ?: return@mapNotNull null).toInt(), it.getLong("timestamp") ?: 0L, it.getString("severity") ?: "")
                })
            } catch (e: Exception) { android.util.Log.e("Serenity", "Error GAD7: ${e.message}") }
        }

        viewModelScope.launch {
            try {
                val snap = db.collection("users").document(uid).collection("pss10_results").orderBy("timestamp").get().await()
                pss10Results.clear()
                pss10Results.addAll(snap.documents.mapNotNull {
                    TestResult((it.getLong("score") ?: return@mapNotNull null).toInt(), it.getLong("timestamp") ?: 0L, it.getString("severity") ?: "")
                })
            } catch (e: Exception) { android.util.Log.e("Serenity", "Error PSS10: ${e.message}") }
        }

        viewModelScope.launch {
            try {
                val snap = db.collection("users").document(uid).collection("phq9_results").orderBy("timestamp").get().await()
                phq9Results.clear()
                phq9Results.addAll(snap.documents.mapNotNull {
                    TestResult((it.getLong("score") ?: return@mapNotNull null).toInt(), it.getLong("timestamp") ?: 0L, it.getString("severity") ?: "")
                })
            } catch (e: Exception) { android.util.Log.e("Serenity", "Error PHQ9: ${e.message}") }
        }
    }

    fun registerMood(emotion: String) {
        MoodRepository.addMood(emotion) { success ->
            if (success) MoodRepository.getMoods { moods -> moodHistory.clear(); moodHistory.addAll(moods) }
        }
    }

    fun clearOnLogout() {
        loaded = false; userName = ""
        moodHistory.clear(); gad7Results.clear(); pss10Results.clear(); phq9Results.clear()
    }

}
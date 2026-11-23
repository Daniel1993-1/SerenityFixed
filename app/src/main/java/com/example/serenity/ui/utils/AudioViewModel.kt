package com.example.serenity.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.serenity.data.repository.AudioRepository

class AudioViewModel : ViewModel() {
    var isPlaying by mutableStateOf(false)
        private set
    var currentType by mutableStateOf("")
        private set

    fun toggleAudio(context: Context, type: String) {
        if (isPlaying && currentType == type) {
            stopAudio()
        } else {
            when (type) {
                "ALPHA" -> AudioRepository.playBinaural(10.0) // 10 Hz
                "THETA" -> AudioRepository.playBinaural(6.0)  // 6 Hz
                "528HZ" -> AudioRepository.play528Hz(context) { stopAudio() }
            }
            isPlaying = true
            currentType = type
        }
    }

    fun stopAudio() {
        AudioRepository.stopAll()
        isPlaying = false
        currentType = ""
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio() // Detener audio si el usuario sale de la pantalla
    }
}
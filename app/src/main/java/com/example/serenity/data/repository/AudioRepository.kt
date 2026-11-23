package com.example.serenity.data.repository

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object AudioRepository {
    private var exoPlayer: ExoPlayer? = null
    private val binauralGenerator = BinauralGenerator()

    // Para la frecuencia 528 Hz (requiere un archivo de audio en assets)
    fun play528Hz(context: Context, onFinished: () -> Unit) {
        stopAll()
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            // NOTA: Necesitas tener un archivo mp3 llamado "solfeggio_528.mp3" en src/main/assets
            val mediaItem = MediaItem.fromUri("asset:///solfeggio_528.mp3")
            setMediaItem(mediaItem)
            prepare()
            play()
            // Listener para cuando termine (opcional)
        }
    }

    // Para Binaural Alpha o Theta
    fun playBinaural(beatFrequency: Double) {
        stopAll()
        binauralGenerator.start(baseFrequency = 200.0, beatFrequency = beatFrequency)
    }

    fun stopAll() {
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null
        binauralGenerator.stop()
    }
}
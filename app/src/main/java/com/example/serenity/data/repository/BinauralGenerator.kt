package com.example.serenity.data.repository

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

class BinauralGenerator {
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var thread: Thread? = null

    fun start(baseFrequency: Double, beatFrequency: Double) {
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
        audioTrack?.play()
        isPlaying = true

        thread = Thread {
            val samples = ShortArray(bufferSize)
            var phaseLeft = 0.0
            var phaseRight = 0.0
            val leftFreq = baseFrequency
            val rightFreq = baseFrequency + beatFrequency

            while (isPlaying) {
                for (i in samples.indices step 2) {
                    samples[i] = (sin(phaseLeft) * Short.MAX_VALUE).toInt().toShort()
                    samples[i + 1] = (sin(phaseRight) * Short.MAX_VALUE).toInt().toShort()
                    phaseLeft += 2 * PI * leftFreq / sampleRate
                    phaseRight += 2 * PI * rightFreq / sampleRate
                }
                audioTrack?.write(samples, 0, samples.size)
            }
        }
        thread?.start()
    }

    fun stop() {
        isPlaying = false
        thread?.join() // Esperar a que el hilo termine
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}
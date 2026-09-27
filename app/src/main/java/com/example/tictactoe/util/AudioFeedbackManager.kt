package com.example.tictactoe.util

import android.media.AudioManager
import android.media.ToneGenerator

/**
 * Handles audio feedback using Android's ToneGenerator.
 */
class AudioFeedbackManager {

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 65)
        } catch (_: Exception) {
            // ToneGenerator initialization may fail on rare emulators/custom ROMs
            toneGenerator = null
        }
    }

    fun playMoveSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
    }

    fun playWinSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 250)
    }

    fun playDrawSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 200)
    }

    fun playResetSound() {
        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 60)
    }

    fun release() {
        try {
            toneGenerator?.release()
        } catch (_: Exception) {
            // Ignore
        } finally {
            toneGenerator = null
        }
    }
}

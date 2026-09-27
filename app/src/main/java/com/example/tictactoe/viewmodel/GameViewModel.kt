package com.example.tictactoe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.data.AppSettings
import com.example.tictactoe.data.SettingsRepository
import com.example.tictactoe.data.ThemeMode
import com.example.tictactoe.engine.GameEngine
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.util.AudioFeedbackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel managing game state, scores, and settings.
 */
class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application.applicationContext)
    private val audioFeedbackManager = AudioFeedbackManager()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings()
    )

    /**
     * Handles a cell tap. Returns true if the move was made, false otherwise.
     */
    fun onCellClicked(index: Int): Boolean {
        val current = _gameState.value
        // If cell is occupied or game is terminal, reject
        if (current.isGameOver || current.board.getOrNull(index) != null) {
            return false
        }

        val updated = GameEngine.makeMove(current, index)
        if (updated != current) {
            _gameState.value = updated

            // Audio feedback
            if (settings.value.soundEnabled) {
                when (updated.status) {
                    is GameStatus.Won -> audioFeedbackManager.playWinSound()
                    is GameStatus.Draw -> audioFeedbackManager.playDrawSound()
                    is GameStatus.InProgress -> audioFeedbackManager.playMoveSound()
                }
            }
            return true
        }
        return false
    }

    /**
     * Resets the board, keeping scores.
     */
    fun onPlayAgain() {
        _gameState.value = GameEngine.resetGame(_gameState.value, resetScores = false)
        if (settings.value.soundEnabled) {
            audioFeedbackManager.playResetSound()
        }
    }

    /**
     * Resets the board and scores.
     */
    fun onResetAll() {
        _gameState.value = GameEngine.resetGame(_gameState.value, resetScores = true)
        if (settings.value.soundEnabled) {
            audioFeedbackManager.playResetSound()
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSoundEnabled(enabled)
        }
    }

    fun updateHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHapticsEnabled(enabled)
        }
    }

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(themeMode)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsRepository.resetSettings()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioFeedbackManager.release()
    }
}

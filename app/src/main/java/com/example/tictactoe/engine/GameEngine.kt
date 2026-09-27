package com.example.tictactoe.engine

import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.model.ScoreBoard

/**
 * Game engine handling all Tic-Tac-Toe rules and state transitions.
 */
object GameEngine {

    val WINNING_COMBINATIONS: List<List<Int>> = listOf(
        // Rows
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        // Columns
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        // Diagonals
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    /**
     * Executes a move on the current [state] at [cellIndex].
     * Returns an updated [GameState] if valid, or the unchanged [state] if the move is rejected.
     */
    fun makeMove(state: GameState, cellIndex: Int): GameState {
        if (cellIndex !in 0..8) return state
        if (state.isGameOver) return state
        if (state.board[cellIndex] != null) return state

        val updatedBoard = state.board.toMutableList().apply {
            this[cellIndex] = state.currentPlayer
        }

        // Check for winning lines
        val winningCombination = findWinningCombination(updatedBoard, state.currentPlayer)
        if (winningCombination != null) {
            val updatedScores = when (state.currentPlayer) {
                Player.X -> state.scoreBoard.copy(xWins = state.scoreBoard.xWins + 1)
                Player.O -> state.scoreBoard.copy(oWins = state.scoreBoard.oWins + 1)
            }
            return state.copy(
                board = updatedBoard,
                status = GameStatus.Won(state.currentPlayer, winningCombination),
                scoreBoard = updatedScores
            )
        }

        // Check for draw (all cells filled without a winner)
        if (updatedBoard.all { it != null }) {
            val updatedScores = state.scoreBoard.copy(draws = state.scoreBoard.draws + 1)
            return state.copy(
                board = updatedBoard,
                status = GameStatus.Draw,
                scoreBoard = updatedScores
            )
        }

        // Switch player and continue
        return state.copy(
            board = updatedBoard,
            currentPlayer = state.currentPlayer.opponent,
            status = GameStatus.InProgress
        )
    }

    /**
     * Resets the board for a new round.
     * @param resetScores If true, scores are also reset to 0.
     */
    fun resetGame(currentState: GameState, resetScores: Boolean = false): GameState {
        return GameState(
            board = List(9) { null },
            currentPlayer = Player.X,
            status = GameStatus.InProgress,
            scoreBoard = if (resetScores) ScoreBoard() else currentState.scoreBoard
        )
    }

    /**
     * Returns the winning combination for [player], or null if none.
     */
    private fun findWinningCombination(board: List<Player?>, player: Player): List<Int>? {
        return WINNING_COMBINATIONS.firstOrNull { combination ->
            combination.all { index -> board[index] == player }
        }
    }
}

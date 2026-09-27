package com.example.tictactoe.model

/**
 * Sealed interface representing the game status.
 */
sealed interface GameStatus {
    data object InProgress : GameStatus
    data class Won(val winner: Player, val winningCells: List<Int>) : GameStatus
    data object Draw : GameStatus
}

/**
 * Tracks match scores.
 */
data class ScoreBoard(
    val xWins: Int = 0,
    val oWins: Int = 0,
    val draws: Int = 0
)

/**
 * Immutable representation of the game state.
 */
data class GameState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val status: GameStatus = GameStatus.InProgress,
    val scoreBoard: ScoreBoard = ScoreBoard()
) {
    val isGameOver: Boolean
        get() = status !is GameStatus.InProgress

    val winningCells: List<Int>
        get() = (status as? GameStatus.Won)?.winningCells ?: emptyList()

    val winner: Player?
        get() = (status as? GameStatus.Won)?.winner

    val isDraw: Boolean
        get() = status is GameStatus.Draw
}

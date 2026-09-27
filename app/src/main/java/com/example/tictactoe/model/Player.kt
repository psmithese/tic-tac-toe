package com.example.tictactoe.model

/**
 * Enum representing the two players.
 */
enum class Player(val symbol: String) {
    X("X"),
    O("O");

    val opponent: Player
        get() = if (this == X) O else X
}

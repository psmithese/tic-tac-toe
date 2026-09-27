package com.example.tictactoe

import com.example.tictactoe.engine.GameEngine
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Deterministic unit test suite covering all game engine rules, winning conditions,
 * move rejections, and match cycles.
 */
class GameEngineTest {

    // Helper to simulate a sequence of moves from an initial state
    private fun playMoves(vararg moves: Int): GameState {
        var state = GameState()
        for (index in moves) {
            state = GameEngine.makeMove(state, index)
        }
        return state
    }

    @Test
    fun playerSwitching_alternatesCorrectlyBetweenXAndO() {
        var state = GameState()
        assertEquals(Player.X, state.currentPlayer)

        state = GameEngine.makeMove(state, 0)
        assertEquals(Player.O, state.currentPlayer)
        assertEquals(Player.X, state.board[0])

        state = GameEngine.makeMove(state, 1)
        assertEquals(Player.X, state.currentPlayer)
        assertEquals(Player.O, state.board[1])
    }

    @Test
    fun occupiedCellRejection_leavesStateUnchanged() {
        var state = GameState()
        state = GameEngine.makeMove(state, 4) // X plays center
        assertEquals(Player.X, state.board[4])
        assertEquals(Player.O, state.currentPlayer)

        // O attempts to play the same cell 4
        val rejectedState = GameEngine.makeMove(state, 4)
        assertEquals("State must be identical when cell is occupied", state, rejectedState)
        assertEquals(Player.O, rejectedState.currentPlayer)
    }

    @Test
    fun invalidCellIndex_isSafelyRejected() {
        val state = GameState()
        val rejectedNegative = GameEngine.makeMove(state, -1)
        val rejectedOverflow = GameEngine.makeMove(state, 9)

        assertEquals(state, rejectedNegative)
        assertEquals(state, rejectedOverflow)
    }

    @Test
    fun xHorizontalWin_row0() {
        // Moves: X:0, O:3, X:1, O:4, X:2 -> X wins top row (0, 1, 2)
        val state = playMoves(0, 3, 1, 4, 2)

        assertTrue(state.isGameOver)
        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(0, 1, 2), status.winningCells)
        assertEquals(1, state.scoreBoard.xWins)
        assertEquals(0, state.scoreBoard.oWins)
    }

    @Test
    fun xHorizontalWin_row1() {
        // Moves: X:3, O:0, X:4, O:1, X:5 -> X wins middle row (3, 4, 5)
        val state = playMoves(3, 0, 4, 1, 5)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(3, 4, 5), status.winningCells)
    }

    @Test
    fun xHorizontalWin_row2() {
        // Moves: X:6, O:0, X:7, O:1, X:8 -> X wins bottom row (6, 7, 8)
        val state = playMoves(6, 0, 7, 1, 8)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(6, 7, 8), status.winningCells)
    }

    @Test
    fun xVerticalWin_col0() {
        // Moves: X:0, O:1, X:3, O:2, X:6 -> X wins col 0 (0, 3, 6)
        val state = playMoves(0, 1, 3, 2, 6)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(0, 3, 6), status.winningCells)
    }

    @Test
    fun xVerticalWin_col1() {
        // Moves: X:1, O:0, X:4, O:2, X:7 -> X wins col 1 (1, 4, 7)
        val state = playMoves(1, 0, 4, 2, 7)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(1, 4, 7), status.winningCells)
    }

    @Test
    fun xVerticalWin_col2() {
        // Moves: X:2, O:0, X:5, O:1, X:8 -> X wins col 2 (2, 5, 8)
        val state = playMoves(2, 0, 5, 1, 8)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(2, 5, 8), status.winningCells)
    }

    @Test
    fun xDiagonalWin_mainDiagonal() {
        // Moves: X:0, O:1, X:4, O:2, X:8 -> X wins diagonal (0, 4, 8)
        val state = playMoves(0, 1, 4, 2, 8)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(0, 4, 8), status.winningCells)
    }

    @Test
    fun xDiagonalWin_antiDiagonal() {
        // Moves: X:2, O:0, X:4, O:1, X:6 -> X wins diagonal (2, 4, 6)
        val state = playMoves(2, 0, 4, 1, 6)

        val status = state.status as GameStatus.Won
        assertEquals(Player.X, status.winner)
        assertEquals(listOf(2, 4, 6), status.winningCells)
    }

    @Test
    fun oHorizontalWin() {
        // Moves: X:0, O:3, X:1, O:4, X:8, O:5 -> O wins middle row (3, 4, 5)
        val state = playMoves(0, 3, 1, 4, 8, 5)

        assertTrue(state.isGameOver)
        val status = state.status as GameStatus.Won
        assertEquals(Player.O, status.winner)
        assertEquals(listOf(3, 4, 5), status.winningCells)
        assertEquals(0, state.scoreBoard.xWins)
        assertEquals(1, state.scoreBoard.oWins)
    }

    @Test
    fun oVerticalWin() {
        // Moves: X:0, O:1, X:3, O:4, X:8, O:7 -> O wins middle column (1, 4, 7)
        val state = playMoves(0, 1, 3, 4, 8, 7)

        val status = state.status as GameStatus.Won
        assertEquals(Player.O, status.winner)
        assertEquals(listOf(1, 4, 7), status.winningCells)
        assertEquals(1, state.scoreBoard.oWins)
    }

    @Test
    fun oDiagonalWin() {
        // Moves: X:1, O:0, X:2, O:4, X:5, O:8 -> O wins diagonal (0, 4, 8)
        val state = playMoves(1, 0, 2, 4, 5, 8)

        val status = state.status as GameStatus.Won
        assertEquals(Player.O, status.winner)
        assertEquals(listOf(0, 4, 8), status.winningCells)
        assertEquals(1, state.scoreBoard.oWins)
    }

    @Test
    fun drawDetection_whenBoardFullWithoutWinner() {
        // Moves sequence producing a classic draw:
        // X O X
        // X X O
        // O X O
        // 0:X, 1:O, 2:X, 4:O, 3:X, 5:O, 7:X, 6:O, 8:X -> wait let's check carefully:
        // Board:
        // 0(X) 1(O) 2(X)
        // 3(X) 4(O) 5(O)
        // 6(O) 7(X) 8(X)
        // Check rows:
        // 0,1,2: X,O,X (no)
        // 3,4,5: X,O,O (no)
        // 6,7,8: O,X,X (no)
        // Check cols:
        // 0,3,6: X,X,O (no)
        // 1,4,7: O,O,X (no)
        // 2,5,8: X,O,X (no)
        // Check diags:
        // 0,4,8: X,O,X (no)
        // 2,4,6: X,O,O (no)
        // Sequence:
        // Move 1: X -> 0
        // Move 2: O -> 1
        // Move 3: X -> 2
        // Move 4: O -> 4
        // Move 5: X -> 3
        // Move 6: O -> 5
        // Move 7: X -> 7
        // Move 8: O -> 6
        // Move 9: X -> 8
        val state = playMoves(0, 1, 2, 4, 3, 5, 7, 6, 8)

        assertTrue(state.isGameOver)
        assertTrue(state.isDraw)
        assertEquals(GameStatus.Draw, state.status)
        assertEquals(1, state.scoreBoard.draws)
        assertEquals(0, state.scoreBoard.xWins)
        assertEquals(0, state.scoreBoard.oWins)
    }

    @Test
    fun gameOverProtection_preventsMovesAfterWin() {
        // X wins at move 5 (top row)
        val winningState = playMoves(0, 3, 1, 4, 2)
        assertTrue(winningState.isGameOver)

        // Attempt move on empty cell 8 after game over
        val moveAfterWin = GameEngine.makeMove(winningState, 8)
        assertEquals(winningState, moveAfterWin)
        assertNull(moveAfterWin.board[8])
    }

    @Test
    fun gameOverProtection_preventsMovesAfterDraw() {
        val drawState = playMoves(0, 1, 2, 4, 3, 5, 7, 6, 8)
        assertTrue(drawState.isGameOver)

        // Any attempt to play after draw is rejected
        val attempt = GameEngine.makeMove(drawState, 0)
        assertEquals(drawState, attempt)
    }

    @Test
    fun gameReset_clearsBoardAndPreservesScores() {
        val wonState = playMoves(0, 3, 1, 4, 2) // X wins
        assertEquals(1, wonState.scoreBoard.xWins)

        val resetState = GameEngine.resetGame(wonState, resetScores = false)

        assertFalse(resetState.isGameOver)
        assertEquals(GameStatus.InProgress, resetState.status)
        assertEquals(Player.X, resetState.currentPlayer)
        assertTrue("All cells must be empty after reset", resetState.board.all { it == null })
        assertEquals("Scores must be preserved", 1, resetState.scoreBoard.xWins)
    }

    @Test
    fun multipleResetCycles_tracksCumulativeScoresCorrectly() {
        // Round 1: X wins
        var state = playMoves(0, 3, 1, 4, 2)
        assertEquals(1, state.scoreBoard.xWins)
        assertEquals(0, state.scoreBoard.oWins)

        // Reset for Round 2
        state = GameEngine.resetGame(state)

        // Round 2: O wins (X:0, O:3, X:1, O:4, X:8, O:5)
        for (move in listOf(0, 3, 1, 4, 8, 5)) {
            state = GameEngine.makeMove(state, move)
        }
        assertEquals(1, state.scoreBoard.xWins)
        assertEquals(1, state.scoreBoard.oWins)

        // Reset for Round 3
        state = GameEngine.resetGame(state)

        // Round 3: Draw
        for (move in listOf(0, 1, 2, 4, 3, 5, 7, 6, 8)) {
            state = GameEngine.makeMove(state, move)
        }
        assertEquals(1, state.scoreBoard.xWins)
        assertEquals(1, state.scoreBoard.oWins)
        assertEquals(1, state.scoreBoard.draws)

        // Complete Reset (including scores)
        val fullReset = GameEngine.resetGame(state, resetScores = true)
        assertEquals(0, fullReset.scoreBoard.xWins)
        assertEquals(0, fullReset.scoreBoard.oWins)
        assertEquals(0, fullReset.scoreBoard.draws)
    }
}

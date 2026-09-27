package com.example.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.theme.GameTheme

/**
 * 3x3 game board composable.
 */
@Composable
fun GameBoard(
    board: List<Player?>,
    winningCells: List<Int>,
    enabled: Boolean,
    hapticsEnabled: Boolean,
    onCellClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = GameTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(colors.boardBackground)
            .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        GameCell(
                            index = index,
                            player = board.getOrNull(index),
                            isWinningCell = index in winningCells,
                            enabled = enabled,
                            hapticsEnabled = hapticsEnabled,
                            onClick = { onCellClicked(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

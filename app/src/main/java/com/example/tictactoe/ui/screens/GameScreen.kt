package com.example.tictactoe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.components.GameBoard
import com.example.tictactoe.ui.components.PlayerIndicator
import com.example.tictactoe.ui.components.ResultDialog
import com.example.tictactoe.ui.theme.GameTheme
import com.example.tictactoe.viewmodel.GameViewModel

/**
 * Main gameplay screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val colors = GameTheme.colors

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tic Tac Toe",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateSettings) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Open Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Player stats and current turn header
            PlayerIndicator(
                currentPlayer = gameState.currentPlayer,
                isGameOver = gameState.isGameOver,
                scoreBoard = gameState.scoreBoard,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Responsive 3x3 Game Board
            GameBoard(
                board = gameState.board,
                winningCells = gameState.winningCells,
                enabled = !gameState.isGameOver,
                hapticsEnabled = settings.hapticsEnabled,
                onCellClicked = { index -> viewModel.onCellClicked(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Game Status Badge
            GameStatusBadge(
                status = gameState.status,
                currentPlayer = gameState.currentPlayer
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Restart Button
            Button(
                onClick = { viewModel.onPlayAgain() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Restart Game",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Result Dialog on terminal state
        ResultDialog(
            status = gameState.status,
            onPlayAgain = { viewModel.onPlayAgain() },
            onBackToHome = onNavigateBack
        )
    }
}

/**
 * Status badge showing current turn or game result.
 */
@Composable
private fun GameStatusBadge(
    status: GameStatus,
    currentPlayer: Player
) {
    val colors = GameTheme.colors

    val (badgeText, badgeColor, indicatorColor) = when (status) {
        is GameStatus.InProgress -> {
            val playerColor = if (currentPlayer == Player.X) colors.playerX else colors.playerO
            Triple("Player ${currentPlayer.symbol}'s Turn", MaterialTheme.colorScheme.surfaceVariant, playerColor)
        }
        is GameStatus.Won -> {
            val winnerColor = if (status.winner == Player.X) colors.playerX else colors.playerO
            Triple("Player ${status.winner.symbol} Won!", colors.winningContainer.copy(alpha = 0.5f), winnerColor)
        }
        is GameStatus.Draw -> {
            Triple("Game Drawn!", colors.winningContainer.copy(alpha = 0.35f), colors.winningGold)
        }
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(badgeColor)
            .border(1.dp, indicatorColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(indicatorColor)
            )
            Text(
                text = badgeText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

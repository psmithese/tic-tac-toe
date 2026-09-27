package com.example.tictactoe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.Player
import com.example.tictactoe.model.ScoreBoard
import com.example.tictactoe.ui.theme.GameTheme

/**
 * Shows both players' scores and whose turn it is.
 */
@Composable
fun PlayerIndicator(
    currentPlayer: Player,
    isGameOver: Boolean,
    scoreBoard: ScoreBoard,
    modifier: Modifier = Modifier
) {
    val colors = GameTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player X Card
        PlayerCard(
            player = Player.X,
            isCurrentTurn = !isGameOver && currentPlayer == Player.X,
            wins = scoreBoard.xWins,
            symbolColor = colors.playerX,
            activeContainerColor = colors.playerXContainer,
            activeGlowColor = colors.playerXGlow,
            modifier = Modifier.weight(1f)
        )

        // Middle Score Pill (Draws count)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${scoreBoard.draws} ties",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontSize = 11.sp
            )
        }

        // Player O Card
        PlayerCard(
            player = Player.O,
            isCurrentTurn = !isGameOver && currentPlayer == Player.O,
            wins = scoreBoard.oWins,
            symbolColor = colors.playerO,
            activeContainerColor = colors.playerOContainer,
            activeGlowColor = colors.playerOGlow,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    isCurrentTurn: Boolean,
    wins: Int,
    symbolColor: Color,
    activeContainerColor: Color,
    activeGlowColor: Color,
    modifier: Modifier = Modifier
) {
    val cardScale by animateFloatAsState(
        targetValue = if (isCurrentTurn) 1.03f else 0.98f,
        animationSpec = tween(220),
        label = "cardScale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isCurrentTurn) activeGlowColor else Color.Transparent,
        animationSpec = tween(220),
        label = "borderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isCurrentTurn) activeContainerColor.copy(alpha = 0.5f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        animationSpec = tween(220),
        label = "backgroundColor"
    )

    val a11yText = "Player ${player.symbol}, $wins wins" + if (isCurrentTurn) ", current turn" else ""

    Box(
        modifier = modifier
            .scale(cardScale)
            .shadow(
                elevation = if (isCurrentTurn) 6.dp else 1.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isCurrentTurn) activeGlowColor else Color.Transparent
            )
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .semantics { contentDescription = a11yText },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isCurrentTurn) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(activeGlowColor)
                    )
                }
                Text(
                    text = "PLAYER ${player.symbol}",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isCurrentTurn) symbolColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = player.symbol,
                style = MaterialTheme.typography.headlineLarge,
                color = symbolColor,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$wins ${if (wins == 1) "Win" else "Wins"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

package com.example.tictactoe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.tictactoe.model.Player
import com.example.tictactoe.ui.theme.GameTheme

/**
 * A single cell in the Tic-Tac-Toe grid.
 */
@Composable
fun GameCell(
    index: Int,
    player: Player?,
    isWinningCell: Boolean,
    enabled: Boolean,
    hapticsEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    val row = (index / 3) + 1
    val col = (index % 3) + 1

    val cellDescription = when (player) {
        Player.X -> "Row $row, Column $col, Player X"
        Player.O -> "Row $row, Column $col, Player O"
        null -> if (enabled) "Row $row, Column $col, Empty cell, tap to play" else "Row $row, Column $col, Empty"
    }

    // Winning cell pulse animation
    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(isWinningCell) {
        if (isWinningCell) {
            pulseScale.animateTo(
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        1f at 0 using FastOutSlowInEasing
                        1.06f at 500 using FastOutSlowInEasing
                        1f at 1000
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            pulseScale.snapTo(1f)
        }
    }

    val colors = GameTheme.colors
    val backgroundColor = when {
        isWinningCell -> colors.winningContainer.copy(alpha = 0.45f)
        else -> colors.cellBackground
    }
    val borderColor = when {
        isWinningCell -> colors.winningGold
        else -> colors.winningGold.copy(alpha = 0.08f)
    }
    val borderWidth = if (isWinningCell) 2.5.dp else 1.dp

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(pulseScale.value)
            .shadow(
                elevation = if (isWinningCell) 8.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isWinningCell) colors.winningGoldGlow else Color.Black,
                spotColor = if (isWinningCell) colors.winningGold else Color.Black
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                enabled = enabled && player == null,
                role = Role.Button,
                onClick = {
                    if (hapticsEnabled) {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    onClick()
                }
            )
            .semantics {
                contentDescription = cellDescription
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = player != null,
            enter = scaleIn(
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
                initialScale = 0.2f
            ),
            exit = scaleOut(animationSpec = tween(durationMillis = 150))
        ) {
            when (player) {
                Player.X -> DrawXSymbol(
                    color = if (isWinningCell) colors.winningGoldGlow else colors.playerX,
                    modifier = Modifier.fillMaxSize()
                )
                Player.O -> DrawOSymbol(
                    color = if (isWinningCell) colors.winningGoldGlow else colors.playerO,
                    modifier = Modifier.fillMaxSize()
                )
                null -> Unit
            }
        }
    }
}

/**
 * Canvas-drawn X symbol.
 */
@Composable
private fun DrawXSymbol(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.padding(6.dp)) {
        val strokeWidth = size.width * 0.16f
        val offsetPadding = size.width * 0.05f

        // Diagonal \
        drawLine(
            color = color,
            start = Offset(offsetPadding, offsetPadding),
            end = Offset(size.width - offsetPadding, size.height - offsetPadding),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        // Diagonal /
        drawLine(
            color = color,
            start = Offset(size.width - offsetPadding, offsetPadding),
            end = Offset(offsetPadding, size.height - offsetPadding),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Canvas-drawn O symbol.
 */
@Composable
private fun DrawOSymbol(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.padding(6.dp)) {
        val strokeWidth = size.width * 0.15f
        val radius = (size.minDimension - strokeWidth) / 2f
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )
    }
}

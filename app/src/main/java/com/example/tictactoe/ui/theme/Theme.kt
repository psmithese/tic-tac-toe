package com.example.tictactoe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class GameColors(
    val playerX: Color,
    val playerXGlow: Color,
    val playerXContainer: Color,
    val playerO: Color,
    val playerOGlow: Color,
    val playerOContainer: Color,
    val winningGold: Color,
    val winningGoldGlow: Color,
    val winningContainer: Color,
    val boardBackground: Color,
    val cellBackground: Color
)

val DarkGameColors = GameColors(
    playerX = PlayerXPrimary,
    playerXGlow = PlayerXGlow,
    playerXContainer = PlayerXContainerDark,
    playerO = PlayerOPrimary,
    playerOGlow = PlayerOGlow,
    playerOContainer = PlayerOContainerDark,
    winningGold = WinningGold,
    winningGoldGlow = WinningGoldGlow,
    winningContainer = WinningGoldContainerDark,
    boardBackground = DarkSurface,
    cellBackground = DarkSurfaceElevated
)

val LightGameColors = GameColors(
    playerX = PlayerXPrimary,
    playerXGlow = PlayerXGlow,
    playerXContainer = PlayerXContainerLight,
    playerO = PlayerOPrimary,
    playerOGlow = PlayerOGlow,
    playerOContainer = PlayerOContainerLight,
    winningGold = WinningGold,
    winningGoldGlow = WinningGoldGlow,
    winningContainer = WinningGoldContainerLight,
    boardBackground = LightSurfaceVariant,
    cellBackground = LightSurface
)

val LocalGameColors = staticCompositionLocalOf { DarkGameColors }

private val DarkColorScheme = darkColorScheme(
    primary = Indigo400,
    onPrimary = Color.White,
    primaryContainer = Indigo600,
    onPrimaryContainer = Indigo100,
    secondary = PlayerXPrimary,
    onSecondary = Color.Black,
    secondaryContainer = PlayerXContainerDark,
    onSecondaryContainer = PlayerXGlow,
    tertiary = PlayerOPrimary,
    onTertiary = Color.White,
    tertiaryContainer = PlayerOContainerDark,
    onTertiaryContainer = PlayerOGlow,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo600,
    onPrimary = Color.White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo600,
    secondary = PlayerXPrimary,
    onSecondary = Color.White,
    secondaryContainer = PlayerXContainerLight,
    onSecondaryContainer = PlayerXPrimary,
    tertiary = PlayerOPrimary,
    onTertiary = Color.White,
    tertiaryContainer = PlayerOContainerLight,
    onTertiaryContainer = PlayerOPrimary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val gameColors = if (darkTheme) DarkGameColors else LightGameColors

    CompositionLocalProvider(LocalGameColors provides gameColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object GameTheme {
    val colors: GameColors
        @Composable
        get() = LocalGameColors.current
}

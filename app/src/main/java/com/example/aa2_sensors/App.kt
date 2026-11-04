package com.example.aa2_sensors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aa2_sensors.game.Level
import com.example.aa2_sensors.ui.screens.GameScreen
import com.example.aa2_sensors.ui.screens.MenuScreen


const val LEVEL_COUNT = 6

sealed interface Screen {
    data object Menu : Screen
    data class InGame(val levelId: Int) : Screen
}

@Composable
fun App() {
    var screen by remember { mutableStateOf<Screen>(Screen.Menu) }

    when (val current = screen) {
        Screen.Menu -> MenuScreen(onLevelClick = { id -> screen = Screen.InGame(id) })
        is Screen.InGame -> GameScreen(
            levelName = "level_${current.levelId}",
            onExit = { screen = Screen.Menu },
            onNextLevel = if (current.levelId < LEVEL_COUNT) {
                { screen = Screen.InGame(current.levelId + 1) }
            } else null
        )
    }
}

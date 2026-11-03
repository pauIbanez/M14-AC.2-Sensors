package com.example.aa2_sensors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aa2_sensors.game.Level
import com.example.aa2_sensors.ui.screens.GameScreen
import com.example.aa2_sensors.ui.screens.MenuScreen


sealed interface Screen {
    data object Menu : Screen
    data class InGame(val levelName: String) : Screen
}

@Composable
fun App() {
    var screen by remember { mutableStateOf<Screen>(Screen.Menu) }

    when (val current = screen) {
        Screen.Menu -> MenuScreen(onPlay = { screen = Screen.InGame("level1") })
        is Screen.InGame -> GameScreen(current.levelName, onExit = { screen = Screen.Menu })
    }
}

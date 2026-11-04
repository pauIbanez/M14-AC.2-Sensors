package com.example.aa2_sensors.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.aa2_sensors.game.Level
import com.example.aa2_sensors.game.LevelData
import com.example.aa2_sensors.game.Status
import com.example.aa2_sensors.sensors.rememberAccelerometer
import com.example.aa2_sensors.ui.game.Attempts
import com.example.aa2_sensors.ui.game.GameCanvas
import com.example.aa2_sensors.ui.game.GoBack
import com.example.aa2_sensors.ui.game.Restart

@Composable
fun GameScreen(levelName: String, onExit: () -> Unit, onNextLevel: (() -> Unit)?) {
    val context = LocalContext.current
    var level by remember(levelName) {
        mutableStateOf(Level(LevelData.load(context, levelName)))
    }
    val accelerometer = rememberAccelerometer()

    LaunchedEffect(level) {
        var last = withFrameNanos { it }
        while (true) {
            withFrameNanos { now ->
                level.tick(accelerometer.rot, (now - last) / 1_000_000_000f)
                last = now
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        GameCanvas(level)
        when (level.status) {
            Status.Won -> {
                Column(Modifier.align(Alignment.Center)) {
                    if (onNextLevel != null) {
                        Button(onClick = onNextLevel) { Text("NEXT LEVEL") }
                    }
                    GoBack({ onExit() }, modifier = Modifier)
                }
            }

            else -> {
                Restart(onClick = { level = Level(level.levelData) })
                Attempts(level.attempts, Modifier.align(Alignment.TopEnd))
                GoBack(
                    { onExit() },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }

    }
}

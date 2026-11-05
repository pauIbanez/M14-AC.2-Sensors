package com.example.aa2_sensors.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aa2_sensors.game.Level
import com.example.aa2_sensors.game.LevelData
import com.example.aa2_sensors.game.Status
import com.example.aa2_sensors.sensors.rememberAccelerometer
import com.example.aa2_sensors.ui.elements.LongTextButton
import com.example.aa2_sensors.ui.elements.WithTitle
import com.example.aa2_sensors.ui.game.Attempts
import com.example.aa2_sensors.ui.game.GameCanvas
import com.example.aa2_sensors.ui.game.GoBack
import com.example.aa2_sensors.ui.game.HomeButton
import com.example.aa2_sensors.ui.game.Marble
import com.example.aa2_sensors.ui.menu.WoodBackground
import com.example.aa2_sensors.ui.theme.LocalButtonSizes

@Composable
fun GameScreen(levelName: String, onExit: () -> Unit, onNextLevel: (() -> Unit)?) {
    val context = LocalContext.current
    var level by remember(levelName) {
        mutableStateOf(Level(LevelData.load(context, levelName)))
    }
    val accelerometer = rememberAccelerometer()
    val marble = remember { Marble() }
    val sizes = LocalButtonSizes.current

    LaunchedEffect(level) {
        var last = withFrameNanos { it }
        while (true) {
            withFrameNanos { now ->
                val dt = (now - last) / 1_000_000_000f
                level.tick(accelerometer.rot, dt)
                marble.updateLight(accelerometer.rot, dt)
                last = now
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        GameCanvas(level, marble)
        when (level.status) {
            Status.Won -> {
                Box(modifier = Modifier.fillMaxSize().background(color = Color(0x5D000000)).align(Alignment.Center)) {

                    WithTitle(levelName, modifier = Modifier.align(Alignment.Center)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(sizes.gap * 2),
                            // modifier = Modifier
                        ) {
                            LongTextButton("RESTART LEVEL", { level = Level(LevelData.load(context, levelName)) })
                            if (onNextLevel != null) {
                                LongTextButton("NEXT LEVEL", onNextLevel)
                            }
                        }
                    }

                    GoBack(onExit)
                }
            }

            else -> {
                Attempts(level.attempts)
                GoBack(onExit)
            }
        }

    }
}

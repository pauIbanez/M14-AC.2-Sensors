package com.example.aa2_sensors.ui.game

import androidx.compose.runtime.Composable
import com.example.aa2_sensors.game.Level
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Modifier

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.aa2_sensors.game.Ball
import com.example.aa2_sensors.game.LevelData


@Composable
fun GameCanvas(level: Level) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .aspectRatio(level.levelData.width / level.levelData.height)

    ) {
        val scale = size.width / level.levelData.width

        drawLevel(level.levelData, scale)
        drawBall(level.ball, scale)
    }
}

private fun DrawScope.drawLevel(levelData: LevelData, scale: Float) {
    drawRect(color = Color.Blue, size = size)

    for ((y, row) in levelData.tiles.withIndex()) {
        for ((x, cell) in row.withIndex()) {
            when (cell) {
                'w' -> {
                    drawRect(
                        color = Color.Gray,
                        topLeft = Offset(x * scale, y * scale),
                        size = Size(scale, scale)
                    )
                }

                's' -> {
                    drawRect(
                        color = Color.DarkGray,
                        topLeft = Offset(x * scale, y * scale),
                        size = Size(scale, scale)
                    )
                }

                'e' -> {
                    drawRect(
                        color = Color.Green,
                        topLeft = Offset(x * scale, y * scale),
                        size = Size(scale, scale)
                    )
                }

                'h' -> {
                    drawCircle(
                        color = Color.Black,
                        radius = 0.5f * scale,
                        center = Offset((x + 0.5f) * scale, (y + 0.5f) * scale),
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBall(ball: Ball, scale: Float) {
    drawCircle(
        color = Color.Red,
        radius = ball.drawnRadius * scale,
        center = Offset(ball.pos.x * scale, ball.pos.y * scale)
    )
}


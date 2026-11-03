package com.example.aa2_sensors.game

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class Level(val levelData: LevelData) {
    var ball by mutableStateOf(Ball(levelData)); private set
    var attempts by mutableIntStateOf(1); private set
    var status by mutableStateOf(Status.Playing); private set

    fun tick(tilt: Vector2, dt: Float) {
        if (status != Status.Playing) return
        ball.update(tilt, dt)

        when {
            ball.isGone -> {
                ball = Ball(levelData)
                attempts++
            }

            ball.isDying -> Unit
            levelData.isEnd(ball.pos) -> status = Status.Won
            levelData.isHole(ball.pos) -> ball.startDying()
        }
    }

}

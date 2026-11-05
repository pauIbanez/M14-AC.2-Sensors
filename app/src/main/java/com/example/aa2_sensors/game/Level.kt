package com.example.aa2_sensors.game

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Level(val levelData: LevelData) {
    var ball by mutableStateOf(Ball(levelData)); private set
    var attempts by mutableIntStateOf(1); private set
    var status by mutableStateOf(Status.Playing); private set
    var startTime: Long = System.currentTimeMillis()
    var endTime: Long = 0
    var attemptTime = endTime - startTime

    fun statAttempt() {
        startTime = System.currentTimeMillis()
    }

    fun endAttempt() {
        endTime = System.currentTimeMillis()
    }

    fun tick(tilt: Vector2, dt: Float) {
        if (status != Status.Playing) return
        ball.update(tilt, dt)

        when {
            ball.isGone -> {
                ball = Ball(levelData)
                attempts++
                statAttempt()
            }

            ball.isDying -> Unit
            levelData.isEnd(ball.pos) -> {
                endAttempt()
                status = Status.Won
            }

            levelData.isHole(ball.pos) -> ball.startDying()
        }
    }

}

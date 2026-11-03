package com.example.aa2_sensors.game

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Ball(levelData: LevelData) {

    companion object {
        const val RADIUS = 0.4f
        const val DEATH_TIME = 1f
    }

    var isGone: Boolean = false; private set
    var isDying: Boolean = false; private set
    var pos by mutableStateOf(levelData.startPos); private set
    var drawnRadius by mutableStateOf(RADIUS); private set


    fun startDying() {}

    fun update(tilt: Vector2, dt: Float) {

    }
}

package com.example.aa2_sensors.game

class Ball(levelData: LevelData) {

    var isGone: Boolean = false; private set
    var isDying: Boolean = false; private set
    var pos: Vector2 = levelData.startPos

    fun startDying() {}

    fun update(tilt: Vector2, dt: Float) {

    }
}

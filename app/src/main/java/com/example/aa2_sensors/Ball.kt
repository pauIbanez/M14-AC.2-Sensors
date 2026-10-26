package com.example.aa2_sensors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

// Position, radius and velocity are in level units, not pixels
class Ball(start: Vector2, val radius: Float = 0.4f) {
    // Compose state, so the Canvas redraws when these change

    var pos by mutableStateOf(Vector2(start.x, start.y))

    private var vel = Vector2(0f, 0f)

    fun update(rot: Vector2, acceleration: Float, dt: Float, level: Level) {
        vel = Vector2(vel.x + rot.y * acceleration * dt, vel.y + rot.x * acceleration * dt)
        pos = Vector2(pos.x + vel.x * dt, pos.y + vel.y * dt)

        // Keep the ball inside the level and stop it when it hits a wall
        if (pos.x < radius) {
            pos = pos.copy(x = radius); vel = vel.copy(x = 0f)
        }
        if (pos.x > level.width - radius) {
            pos = pos.copy(x = level.width - radius); vel = vel.copy(x = 0f)
        }
        if (pos.y < radius) {
            pos = pos.copy(y = radius); vel = vel.copy(y = 0f)
        }
        if (pos.y > level.height - radius) {
            pos = pos.copy(y = level.height - radius); vel = vel.copy(y = 0f)
        }
    }

    fun draw(scope: DrawScope, level: Level) {
        val scale = scope.size.width / level.width
        scope.drawCircle(color = Color.Red, radius = radius * scale, center = Offset(pos.x * scale, pos.y * scale))
    }
}

package com.example.aa2_sensors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.floor

// Position, radius and velocity are in level units, not pixels
class Ball(start: Vector2, val radius: Float = 0.4f) {
    // Compose state, so the Canvas redraws when these change

    var pos by mutableStateOf(Vector2(start.x, start.y))

    private var vel = Vector2(0f, 0f)

    fun update(rot: Vector2, acceleration: Float, dt: Float, level: Level) {
        vel = Vector2(vel.x + rot.y * acceleration * dt, vel.y + rot.x * acceleration * dt)

        val delta = Vector2(vel.x * dt, vel.y * dt)
        val newX = pos.x + delta.x
        val newY = pos.y + delta.y

        var top = pos.y - radius
        var bottom = pos.y + radius - 0.001f
        var left = newX - radius + 0.01f
        var right = newX + radius - 0.001f


        var correctedX = newX
        if (delta.x > 0) {
            if (level.isSolidAt(Vector2(right, top)) || level.isSolidAt(Vector2(right, bottom))) {
                correctedX = floor(right) - radius
                vel = vel.copy(x = 0f)
            }
        } else if (delta.x < 0) {
            if (level.isSolidAt(Vector2(left, top)) || level.isSolidAt(Vector2(left, bottom))) {
                correctedX = floor(left) + 1 + radius
                vel = vel.copy(x = 0f)
            }
        }

        top = newY - radius
        bottom = newY + radius - 0.001f
        left = correctedX - radius + 0.01f
        right = correctedX + radius - 0.001f

        var correctedY = newY
        if (delta.y > 0) {
            if (level.isSolidAt(Vector2(left, bottom)) || level.isSolidAt(Vector2(right, bottom))) {
                correctedY = floor(bottom) - radius
                vel = vel.copy(y = 0f)
            }
        } else if (delta.y < 0) {
            if (level.isSolidAt(Vector2(left, top)) || level.isSolidAt(Vector2(right, top))) {
                correctedY = floor(top) + 1 + radius
                vel = vel.copy(y = 0f)
            }
        }

        pos = Vector2(correctedX, correctedY)

    }

    fun draw(scope: DrawScope, level: Level) {
        val scale = scope.size.width / level.width
        scope.drawCircle(color = Color.Red, radius = radius * scale, center = Offset(pos.x * scale, pos.y * scale))
    }
}

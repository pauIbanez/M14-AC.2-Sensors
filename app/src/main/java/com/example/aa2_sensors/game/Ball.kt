package com.example.aa2_sensors.game

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

class Ball(private val levelData: LevelData) {

    companion object {
        const val RADIUS = 0.4f
        const val DEATH_TIME = 1f
        const val ACCELERATION = 1f
    }

    var isGone: Boolean = false; private set
    var isDying: Boolean = false; private set
    var pos by mutableStateOf(levelData.startPos); private set
    var drawnRadius by mutableStateOf(RADIUS); private set

    private var deathAnimTime = 0f
    private var vel = Vector2(0f, 0f)


    fun startDying() {
        vel = Vector2(0f, 0f)
        deathAnimTime = 0f
        isDying = true
    }

    fun update(tilt: Vector2, dt: Float) {
        if (isGone) return

        if (isDying) {
            deathAnimTime += dt
            if (deathAnimTime >= DEATH_TIME) {
                isDying = false
                drawnRadius = 0f
                isGone = true
                return
            }
            val prop = deathAnimTime / DEATH_TIME

            val target = Vector2(pos.x.toInt() + 0.5f, pos.y.toInt() + 0.5f)
            if (Vector2.distance(target, pos) > 0.01f) {
                val dir = (target - pos).normalized
                pos += dir * 2f * dt
            }

            if (prop < 0.2f) return
            drawnRadius = max(0f, drawnRadius - 0.6f * dt)
            return
        }

        vel += Vector2(tilt.y, tilt.x) * ACCELERATION * dt

        // I had a "AABB-based" collision system (though point samplig vs tile map rather than bound-box pair)
        // However AI suggested a crcle-vs-tile, obviously better, so i let it put it in

        val distance = max(abs(vel.x), abs(vel.y)) * dt
        val steps = ceil(distance / (RADIUS * 0.5f)).toInt().coerceAtLeast(1)
        val stepDt = dt / steps

        var next = pos
        repeat(steps) {
            next += vel * stepDt

            for (cy in floor(next.y - RADIUS).toInt()..floor(next.y + RADIUS).toInt()) {
                for (cx in floor(next.x - RADIUS).toInt()..floor(next.x + RADIUS).toInt()) {
                    if (!levelData.isWall(Vector2(cx + 0.5f, cy + 0.5f))) continue


                    val closest = Vector2(
                        next.x.coerceIn(cx.toFloat(), cx + 1f),
                        next.y.coerceIn(cy.toFloat(), cy + 1f)
                    )
                    val offset = next - closest
                    val dist = offset.magnitude
                    if (dist >= RADIUS || dist == 0f) continue


                    val normal = offset.normalized
                    next += normal * (RADIUS - dist)


                    val intoWall = Vector2.dot(vel, normal)
                    if (intoWall < 0) vel -= normal * intoWall
                }
            }
        }

        pos = next
    }
}

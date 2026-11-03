package com.example.aa2_sensors


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class Level(val ball: Ball, val onComplete: () -> Unit, val onDeath: () -> Unit) {

    val width = 19f
    val height = 13f

    var startPos = Vector2(0f, 0f)
    var attempts = 0

    val maze = arrayOf(
        arrayOf('w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'),
        arrayOf('w', ' ', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'),
        arrayOf('w', ' ', 'h', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', 'w', 'w', 'w', 'w', ' ', 'w'),
        arrayOf('s', ' ', ' ', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', ' ', ' ', 'w', ' ', 'w'),
        arrayOf('w', 'w', 'w', 'w', ' ', 'w', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', 'w', 'w', ' ', 'w'),
        arrayOf('w', ' ', ' ', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', ' ', ' ', ' ', ' ', 'w'),
        arrayOf('w', ' ', 'w', 'w', 'w', ' ', 'w', ' ', 'w', 'w', 'w', ' ', 'w', 'w', 'w', 'w', 'w', 'w', 'w'),
        arrayOf('w', ' ', ' ', ' ', ' ', ' ', 'w', ' ', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'),
        arrayOf('w', ' ', ' ', ' ', 'w', 'w', 'w', 'w', 'w', ' ', 'w', 'w', 'w', ' ', 'w', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', 'h', ' ', 'h', 'w', 'w', ' ', 'w', 'w', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'e'),
        arrayOf('w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w')
    )

    init {
        getStartPos()
    }

    private fun getStartPos() {
        for ((y, row) in maze.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == 's') startPos = Vector2(x + 0.5f, y + 0.5f)
            }
        }
    }

    fun isSolidAt(pos: Vector2): Boolean {
        if (pos.x < 0 || pos.y < 0) return true

        val x = pos.x.toInt()
        val y = pos.y.toInt()
        if (x >= width || y >= height) return true

        return maze[y][x] == 'w'
    }

    fun isTouchingHole(pos: Vector2): Boolean {
        if (pos.x < 0 || pos.y < 0) return false
        val x = pos.x.toInt()
        val y = pos.y.toInt()
        if (x >= width || y >= height) return false

        return maze[y][x] == 'h'
    }

    fun start() {
        ball.pos = startPos
        attempts++
        ball.live()
    }

    fun update() {
        if (!ball.isAlive) return

        if (isTouchingHole(ball.pos)) {
            ball.die()
        }
    }


    fun draw(scope: DrawScope) {
        val scale = scope.size.width / width

        scope.drawRect(
            color = Color.Blue,
            topLeft = Offset(0f, 0f),
            size = Size(width * scale, height * scale)
        )

        for ((y, row) in maze.withIndex()) {
            for ((x, cell) in row.withIndex()) {

                when (cell) {
                    'w' -> {
                        scope.drawRect(
                            color = Color.Gray,
                            topLeft = Offset(x * scale, y * scale),
                            size = Size(scale, scale)
                        )
                    }

                    'h' -> {
                        scope.drawCircle(
                            color = Color.Black,
                            radius = 0.5f * scale,
                            center = Offset((x + 0.5f) * scale, (y + 0.5f) * scale),
                        )
                    }
                }
            }
        }
    }
}

package com.example.aa2_sensors


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class Level {

    val width = 19f
    val height = 13f

    var start = Vector2(0f, 0f)

    val maze = arrayOf(
        arrayOf('w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'),
        arrayOf('w', ' ', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'),
        arrayOf('w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', 'w', 'w', 'w', 'w', ' ', 'w'),
        arrayOf('s', ' ', 'w', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', ' ', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', 'w', 'w', 'w', 'w', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', 'w', 'w', ' ', 'w'),
        arrayOf('w', ' ', ' ', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', ' ', ' ', ' ', ' ', 'w'),
        arrayOf('w', ' ', 'w', 'w', 'w', ' ', 'w', ' ', 'w', 'w', 'w', ' ', 'w', 'w', 'w', 'w', 'w', 'w', 'w'),
        arrayOf('w', ' ', 'w', ' ', ' ', ' ', 'w', ' ', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'),
        arrayOf('w', ' ', 'w', ' ', 'w', 'w', 'w', 'w', 'w', ' ', 'w', 'w', 'w', ' ', 'w', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', 'w', ' ', ' ', ' ', ' ', ' ', 'w', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', 'w', 'w', 'w', 'w', 'w', ' ', 'w', 'w', 'w', ' ', 'w', ' ', 'w', ' ', 'w', ' ', 'w'),
        arrayOf('w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w', ' ', 'w', ' ', 'e'),
        arrayOf('w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w')
    )

    init {
        getStart()
    }

    private fun getStart() {
        for ((y, row) in maze.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == 's') start = Vector2(x + 0.5f, y + 0.5f)
            }
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
                            color = Color.Black,
                            topLeft = Offset(x * scale, y * scale),
                            size = Size(scale, scale)
                        )
                    }
                }
            }
        }
    }
}

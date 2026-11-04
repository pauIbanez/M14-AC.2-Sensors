package com.example.aa2_sensors.ui.game

import androidx.compose.runtime.Composable
import com.example.aa2_sensors.game.Level
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas as ImageCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.example.aa2_sensors.game.Ball
import com.example.aa2_sensors.game.LevelData
import com.example.aa2_sensors.ui.tiles.drawWoodMap
import kotlin.math.floor
import kotlin.math.min


@Composable
fun GameCanvas(level: Level) {
    val levelData = level.levelData

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .aspectRatio(levelData.width / levelData.height)
            .drawWithCache {
                val tileSize = tileSizeFor(levelData, size)
                val board = bakeBoard(levelData, tileSize)
                val origin = boardOrigin(levelData, size, tileSize)

                onDrawBehind { drawImage(board, topLeft = origin) }
            }
    ) {
        val tileSize = tileSizeFor(levelData, size)
        val origin = boardOrigin(levelData, size, tileSize)

        translate(origin.x, origin.y) {
            drawBall(level.ball, tileSize)
        }
    }
}

private fun tileSizeFor(levelData: LevelData, area: Size): Float =
    floor(min(area.width / levelData.width, area.height / levelData.height)).coerceAtLeast(1f)

private fun boardOrigin(levelData: LevelData, area: Size, tileSize: Float) = Offset(
    x = floor((area.width - levelData.width * tileSize) / 2f),
    y = floor((area.height - levelData.height * tileSize) / 2f)
)

private fun CacheDrawScope.bakeBoard(levelData: LevelData, tileSize: Float): ImageBitmap {
    val width = (levelData.width * tileSize).toInt()
    val height = (levelData.height * tileSize).toInt()
    val board = ImageBitmap(width, height)

    CanvasDrawScope().draw(
        density = this,
        layoutDirection = layoutDirection,
        canvas = ImageCanvas(board),
        size = Size(width.toFloat(), height.toFloat())
    ) {
        drawWoodMap(levelData.tiles, tileSize)
        drawMarkers(levelData, tileSize)
    }

    return board
}

private fun DrawScope.drawMarkers(levelData: LevelData, scale: Float) {
    for ((y, row) in levelData.tiles.withIndex()) {
        for ((x, cell) in row.withIndex()) {
            when (cell) {
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

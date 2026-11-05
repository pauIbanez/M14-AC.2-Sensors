package com.example.aa2_sensors.ui.game

import androidx.compose.runtime.Composable
import com.example.aa2_sensors.game.Level
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas as ImageCanvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.example.aa2_sensors.game.LevelData
import com.example.aa2_sensors.ui.tiles.EndCarpet
import com.example.aa2_sensors.ui.tiles.StartCarpet
import com.example.aa2_sensors.ui.tiles.drawCarpetTile
import com.example.aa2_sensors.ui.tiles.drawWoodHole
import com.example.aa2_sensors.ui.tiles.drawWoodMap
import kotlin.math.floor
import kotlin.math.min


@Composable
fun GameCanvas(level: Level, marble: Marble) {
    val levelData = level.levelData

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val tileSize = tileSizeFor(levelData, size)
                val origin = boardOrigin(levelData, size, tileSize)
                val board = bakeBoard(levelData, tileSize, origin)

                onDrawBehind { drawImage(board) }
            }
    ) {
        val tileSize = tileSizeFor(levelData, size)
        val origin = boardOrigin(levelData, size, tileSize)

        translate(origin.x, origin.y) {
            drawMarble(marble, level.ball, tileSize)
        }
    }
}

private fun tileSizeFor(levelData: LevelData, area: Size): Float =
    floor(min(area.width / levelData.width, area.height / levelData.height)).coerceAtLeast(1f)

private fun boardOrigin(levelData: LevelData, area: Size, tileSize: Float) = Offset(
    x = floor((area.width - levelData.width * tileSize) / 2f),
    y = floor((area.height - levelData.height * tileSize) / 2f)
)

private fun CacheDrawScope.bakeBoard(levelData: LevelData, tileSize: Float, origin: Offset): ImageBitmap {
    val width = size.width.toInt()
    val height = size.height.toInt()
    val board = ImageBitmap(width, height)

    CanvasDrawScope().draw(
        density = this,
        layoutDirection = layoutDirection,
        canvas = ImageCanvas(board),
        size = Size(width.toFloat(), height.toFloat())
    ) {
        drawWoodMap(levelData.tiles, tileSize, origin)
        translate(origin.x, origin.y) {
            drawMarkers(levelData, tileSize)
        }
    }

    return board
}

private fun DrawScope.drawMarkers(levelData: LevelData, scale: Float) {
    for ((y, row) in levelData.tiles.withIndex()) {
        for ((x, cell) in row.withIndex()) {
            val topLeft = Offset(x * scale, y * scale)
            when (cell) {
                's' -> drawCarpetTile(StartCarpet, scale, topLeft)
                'e' -> drawCarpetTile(EndCarpet, scale, topLeft)
                'h' -> drawWoodHole(scale, topLeft)
            }
        }
    }
}

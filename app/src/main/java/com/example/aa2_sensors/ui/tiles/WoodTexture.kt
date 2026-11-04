package com.example.aa2_sensors.ui.tiles

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.sin

/**
 *
 * COMPLETLEY AI GENERATED
 */


private const val GRAIN_SEGMENTS = 6
private const val FINE_GRAIN_WIDTH = 0.014f
private const val BROAD_GRAIN_WIDTH = 0.11f
private const val BROAD_GRAIN_ALPHA = 0.22f

class WoodStyle(
    val tones: List<Color>,
    val grainDark: Color,
    val grainLight: Color,
    val seam: Color?,
    val plankHeight: Int,
    val plankLength: Int,
    val grainPerPlank: Int,
    val knotChance: Float,
    val seed: Int,
)

/** Pale maple boards: the surface the ball rolls on. */
val FloorWood = WoodStyle(
    tones = listOf(
        Color(0xFFD2A86F),
        Color(0xFFC99D63),
        Color(0xFFD8B27C),
        Color(0xFFC29359),
    ),
    grainDark = Color(0x557A4E22),
    grainLight = Color(0x33FFE3B0),
    seam = Color(0xFF6E4520),
    plankHeight = 2,
    plankLength = 6,
    grainPerPlank = 16,
    knotChance = 0.04f,
    seed = 11,
)

/** One seamless slab in the orange tone of the menu buttons. */
val WallWood = WoodStyle(
    tones = listOf(Color(0xFFC8641C)),
    grainDark = Color(0x666E2A0A),
    grainLight = Color(0x40FFB65C),
    seam = null,
    plankHeight = 3,
    plankLength = 1_000_000,
    grainPerPlank = 30,
    knotChance = 0.06f,
    seed = 29,
)

/**
 * Paints one tile of wood at the origin. Everything comes from the world position,
 * so the grain and plank joints carry on across neighbouring tiles.
 */
fun DrawScope.drawWood(style: WoodStyle, worldX: Int, worldY: Int, tileSize: Float) {
    val plank = worldY.floorDiv(style.plankHeight)
    val row = worldY - plank * style.plankHeight
    val stagger = (hash(plank, style.seed) * style.plankLength).toInt()
    val board = (worldX + stagger).floorDiv(style.plankLength)
    val tone = style.tones[(hash(plank, board, style.seed) * style.tones.size).toInt()]

    drawRect(color = tone, size = Size(tileSize, tileSize))

    val grain = GrainArea(style, plank, board, row, worldX, tileSize)
    drawGrain(grain, count = style.grainPerPlank / 4, salt = 2, width = BROAD_GRAIN_WIDTH, alpha = BROAD_GRAIN_ALPHA)
    drawGrain(grain, count = style.grainPerPlank, salt = 1, width = FINE_GRAIN_WIDTH, alpha = 1f)

    if (hash(worldX, worldY, style.seed + 3) < style.knotChance) {
        drawKnot(style, worldX, worldY, tileSize)
    }

    style.seam?.let { seam ->
        val isBoardStart = (worldX + stagger).mod(style.plankLength) == 0
        drawSeams(seam, drawTop = row == 0, drawLeft = isBoardStart, tileSize = tileSize)
    }
}

private class GrainArea(
    val style: WoodStyle,
    val plank: Int,
    val board: Int,
    val row: Int,
    val worldX: Int,
    val tileSize: Float,
)

private fun DrawScope.drawGrain(area: GrainArea, count: Int, salt: Int, width: Float, alpha: Float) {
    val dark = Path()
    val light = Path()
    val seed = area.style.seed + salt * 101

    for (line in 0 until count) {
        val key = area.board * 31 + line
        val y = hash(area.plank, key, seed) * area.style.plankHeight - area.row
        if (y < -0.2f || y > 1.2f) continue

        val amplitude = 0.02f + 0.06f * hash(area.plank, key, seed + 1)
        val frequency = 0.5f + 1.2f * hash(area.plank, key, seed + 2)
        val phase = 2f * PI.toFloat() * hash(area.plank, key, seed + 3)
        val target = if (hash(area.plank, key, seed + 4) > 0.3f) dark else light

        for (step in -1..GRAIN_SEGMENTS + 1) {
            val localX = step.toFloat() / GRAIN_SEGMENTS
            val worldX = area.worldX + localX
            val wave = sin(worldX * frequency + phase) +
                0.4f * sin(worldX * frequency * 2.7f + phase * 1.7f)
            val px = localX * area.tileSize
            val py = (y + amplitude * wave) * area.tileSize
            if (step == -1) target.moveTo(px, py) else target.lineTo(px, py)
        }
    }

    val stroke = Stroke(width = max(1f, area.tileSize * width))
    val style = area.style
    drawPath(dark, color = style.grainDark.copy(alpha = style.grainDark.alpha * alpha), style = stroke)
    drawPath(light, color = style.grainLight.copy(alpha = style.grainLight.alpha * alpha), style = stroke)
}

private fun DrawScope.drawKnot(style: WoodStyle, worldX: Int, worldY: Int, tileSize: Float) {
    val centerX = (0.3f + 0.4f * hash(worldX, worldY, style.seed + 4)) * tileSize
    val centerY = (0.3f + 0.4f * hash(worldX, worldY, style.seed + 5)) * tileSize
    val radiusX = (0.1f + 0.08f * hash(worldX, worldY, style.seed + 6)) * tileSize
    val radiusY = radiusX * 0.6f
    val ringStroke = Stroke(width = max(1f, tileSize * 0.012f))

    for (ring in 3 downTo 1) {
        val scale = ring / 3f
        drawOval(
            color = style.grainDark,
            topLeft = Offset(centerX - radiusX * scale, centerY - radiusY * scale),
            size = Size(radiusX * 2f * scale, radiusY * 2f * scale),
            style = ringStroke,
        )
    }
    drawOval(
        color = style.grainDark,
        topLeft = Offset(centerX - radiusX / 3f, centerY - radiusY / 3f),
        size = Size(radiusX * 2f / 3f, radiusY * 2f / 3f),
    )
}

private fun DrawScope.drawSeams(seam: Color, drawTop: Boolean, drawLeft: Boolean, tileSize: Float) {
    val width = max(1f, tileSize * 0.016f)
    val highlight = Color(0x26FFFFFF)

    if (drawTop) {
        drawRect(color = seam, size = Size(tileSize, width))
        drawRect(color = highlight, topLeft = Offset(0f, width), size = Size(tileSize, width))
    }
    if (drawLeft) {
        drawRect(color = seam, size = Size(width, tileSize))
        drawRect(color = highlight, topLeft = Offset(width, 0f), size = Size(width, tileSize))
    }
}

/** Repeatable noise in 0..1, so a position always gets the same wood. */
private fun hash(a: Int, b: Int, c: Int = 0): Float {
    var h = a * 374761393 + b * 668265263 + c * 1442695041
    h = (h xor (h ushr 13)) * 1274126177
    h = h xor (h ushr 16)
    return (h and 0xFFFFFF) / 16777216f
}

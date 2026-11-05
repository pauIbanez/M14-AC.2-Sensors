package com.example.aa2_sensors.ui.tiles

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.max

/**
 *
 * COMPLETLEY AI GENERATED
 */


/** Floor left visible around the carpet, as a fraction of a tile. It also holds the cast shadow. */
private const val CARPET_MARGIN = 0.06f
private const val CARPET_CORNER = 0.03f

private const val SHADOW_STEPS = 3
private const val SHADOW_STEP_SIZE = 0.012f

private const val PILE_RIBS = 16

private const val TRIM_INSET = 0.13f
private const val TRIM_WIDTH = 0.03f

val StartCarpet = Color(0xFF8C2F2B)
val EndCarpet = Color(0xFF3F7A4A)

private val CarpetTrim = Color(0xFFC2A25A)
private val PileDark = Color(0x22000000)
private val PileLight = Color(0x14FFFFFF)
private val SheenLight = Color(0x1FFFFFFF)
private val SheenDark = Color(0x26000000)

/** Lays a small carpet of the given [color] over a floor tile. */
fun DrawScope.drawCarpetTile(color: Color, tileSize: Float, topLeft: Offset = Offset.Zero) {
    val inset = CARPET_MARGIN * tileSize
    val side = tileSize - 2f * inset
    val corner = CornerRadius(CARPET_CORNER * tileSize)
    val carpet = Path().apply {
        addRoundRect(RoundRect(Rect(Offset(inset, inset), Size(side, side)), corner))
    }

    translate(topLeft.x, topLeft.y) {
        drawCarpetShadow(carpet, tileSize)
        clipPath(carpet) {
            drawRect(color = color, size = Size(tileSize, tileSize))
            drawPile(tileSize)
            drawSheen(tileSize)
        }
        drawTrim(corner, tileSize)
    }
}

/** A carpet barely lifts off the floor, so its shadow is much shorter than a wall's. */
private fun DrawScope.drawCarpetShadow(carpet: Path, tileSize: Float) {
    val step = SHADOW_STEP_SIZE * tileSize
    for (layer in 1..SHADOW_STEPS) {
        translate(layer * step, layer * step) {
            drawPath(carpet, color = ShadowLayer)
        }
    }
}

/** Alternating ribs along the carpet, the way a woven pile catches the light. */
private fun DrawScope.drawPile(tileSize: Float) {
    val spacing = tileSize / PILE_RIBS
    val width = max(1f, spacing / 2f)

    for (rib in 0..PILE_RIBS) {
        val x = rib * spacing
        drawLine(
            color = if (rib % 2 == 0) PileDark else PileLight,
            start = Offset(x, 0f),
            end = Offset(x, tileSize),
            strokeWidth = width,
        )
    }
}

/** The light comes from the top left, so the pile fades darker towards the bottom right. */
private fun DrawScope.drawSheen(tileSize: Float) {
    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(SheenLight, Color.Transparent, SheenDark),
            start = Offset.Zero,
            end = Offset(tileSize, tileSize),
        ),
        size = Size(tileSize, tileSize),
    )
}

private fun DrawScope.drawTrim(corner: CornerRadius, tileSize: Float) {
    val inset = TRIM_INSET * tileSize
    val side = tileSize - 2f * inset

    drawRoundRect(
        color = CarpetTrim,
        topLeft = Offset(inset, inset),
        size = Size(side, side),
        cornerRadius = corner,
        style = Stroke(width = max(1f, TRIM_WIDTH * tileSize)),
    )
}

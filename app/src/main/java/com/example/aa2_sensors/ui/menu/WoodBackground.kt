package com.example.aa2_sensors.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private const val PLANK_COUNT = 5
private const val GRAIN_LINES_PER_PLANK = 14

/**
 *
 * COMPLETLEY AI GENERATED THIS BACKGROUND
 */

private val PlankTones = listOf(
    Color(0xFF3B2012),
    Color(0xFF442615),
    Color(0xFF341B0F),
    Color(0xFF3F2313),
)
private val GrainColor = Color(0x2E140702)
private val SeamColor = Color(0xFF170B05)
private val SeamHighlight = Color(0x1FFFB871)
private val CenterGlow = Color(0x38FF9A3D)
private val EdgeShade = Color(0xA60A0402)

@Composable
fun WoodBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // Fixed seed so the grain stays the same on every redraw
        val random = Random(7)
        val plankHeight = size.height / PLANK_COUNT

        for (plank in 0 until PLANK_COUNT) {
            drawPlank(plank, top = plank * plankHeight, height = plankHeight, random = random)
        }
        drawLighting()
    }
}

private fun DrawScope.drawPlank(index: Int, top: Float, height: Float, random: Random) {
    val tone = PlankTones[index % PlankTones.size]
    drawRect(color = tone, topLeft = Offset(0f, top), size = Size(size.width, height))

    repeat(GRAIN_LINES_PER_PLANK) {
        val y = top + random.nextFloat() * height
        val startX = random.nextFloat() * size.width * 0.8f
        val length = size.width * (0.15f + random.nextFloat() * 0.45f)
        val drift = (random.nextFloat() - 0.5f) * height * 0.08f
        drawLine(
            color = GrainColor,
            start = Offset(startX, y),
            end = Offset(startX + length, y + drift),
            strokeWidth = (1f + random.nextFloat() * 1.5f).dp.toPx()
        )
    }

    if (index > 0) {
        val seamHeight = 2.dp.toPx()
        drawRect(color = SeamColor, topLeft = Offset(0f, top), size = Size(size.width, seamHeight))
        drawRect(
            color = SeamHighlight,
            topLeft = Offset(0f, top + seamHeight),
            size = Size(size.width, 1.dp.toPx())
        )
    }
}

private fun DrawScope.drawLighting() {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(CenterGlow, Color.Transparent),
            center = center,
            radius = size.minDimension * 0.9f
        )
    )
    drawRect(
        brush = Brush.radialGradient(
            0.45f to Color.Transparent,
            1f to EdgeShade,
            center = center,
            radius = size.maxDimension * 0.62f
        )
    )
}

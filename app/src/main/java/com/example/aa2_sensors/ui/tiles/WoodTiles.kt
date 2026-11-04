package com.example.aa2_sensors.ui.tiles

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * COMPLETLEY AI GENERATED
 */


/** Floor left visible on open sides, as a fraction of a tile. It also holds the cast shadow. */
private const val WALL_MARGIN = 0.08f
private const val OUTER_RADIUS = 0.14f

/** How far the shape carries on past joined sides, so joins never get a bevel. */
private const val OVERHANG = 0.5f
private const val QUADRANT_OVERLAP = 0.02f
private const val ARC_STEPS = 6

private const val SHADOW_STEPS = 6
private const val BEVEL_STEPS = 5
private const val BEVEL_STEP_SIZE = 0.012f

private val ShadowLayer = Color(0x1E000000)
private val BevelLight = Color(0x2EFFD9A0)
private val BevelDark = Color(0x33200800)

@Composable
fun WoodWallTile(mask: Int, modifier: Modifier = Modifier, worldX: Int = 0, worldY: Int = 0) {
    Canvas(modifier = modifier) {
        drawWoodWallTile(mask, worldX, worldY, size.minDimension)
    }
}

@Composable
fun WoodFloorTile(modifier: Modifier = Modifier, worldX: Int = 0, worldY: Int = 0) {
    Canvas(modifier = modifier) {
        drawWoodFloorTile(worldX, worldY, size.minDimension)
    }
}

/**
 * Draws a map in the level file format: [wall] cells are walls, anything else is floor.
 * Keep [tileSize] a whole number of pixels, otherwise hairline gaps show between tiles.
 */
fun DrawScope.drawWoodMap(rows: List<String>, tileSize: Float, wall: Char = 'w') {
    val columns = rows.maxOf { it.length }

    // Off-map counts as wall, so the map's outer edge has no bevel
    fun isWall(x: Int, y: Int): Boolean {
        if (y !in rows.indices || x !in 0 until columns) return true
        return rows[y].getOrNull(x) == wall
    }

    for (y in rows.indices) {
        for (x in 0 until columns) {
            val topLeft = Offset(x * tileSize, y * tileSize)
            if (isWall(x, y)) {
                drawWoodWallTile(blobMaskAt(x, y, ::isWall), x, y, tileSize, topLeft)
            } else {
                drawWoodFloorTile(x, y, tileSize, topLeft)
            }
        }
    }
}

fun DrawScope.drawWoodFloorTile(
    worldX: Int,
    worldY: Int,
    tileSize: Float,
    topLeft: Offset = Offset.Zero,
) {
    translate(topLeft.x, topLeft.y) {
        clipRect(0f, 0f, tileSize, tileSize) {
            drawWood(FloorWood, worldX, worldY, tileSize)
        }
    }
}

fun DrawScope.drawWoodWallTile(
    mask: Int,
    worldX: Int,
    worldY: Int,
    tileSize: Float,
    topLeft: Offset = Offset.Zero,
) {
    val shape = wallShape(mask, tileSize)

    translate(topLeft.x, topLeft.y) {
        clipRect(0f, 0f, tileSize, tileSize) {
            drawWood(FloorWood, worldX, worldY, tileSize)
            drawCastShadow(shape, tileSize)
            clipPath(shape) {
                drawWood(WallWood, worldX, worldY, tileSize)
                drawBevel(shape, tileSize)
            }
        }
    }
}

/** The light comes from the top left, so the shadow falls towards the bottom right. */
private fun DrawScope.drawCastShadow(shape: Path, tileSize: Float) {
    val step = WALL_MARGIN * tileSize / SHADOW_STEPS
    for (layer in 1..SHADOW_STEPS) {
        drawPath(shape.shifted(layer * step, layer * step), color = ShadowLayer)
    }
}

/** Comparing the shape with shifted copies of itself lights every edge, corner and curve alike. */
private fun DrawScope.drawBevel(shape: Path, tileSize: Float) {
    val step = BEVEL_STEP_SIZE * tileSize
    for (layer in 1..BEVEL_STEPS) {
        val offset = layer * step
        val lit = Path.combine(PathOperation.Difference, shape, shape.shifted(offset, offset))
        val shaded = Path.combine(PathOperation.Difference, shape, shape.shifted(-offset, -offset))
        drawPath(lit, color = BevelLight)
        drawPath(shaded, color = BevelDark)
    }
}

private fun Path.shifted(x: Float, y: Float): Path {
    val source = this
    return Path().apply { addPath(source, Offset(x, y)) }
}

/** Builds the wall outline one corner of the tile at a time. */
private fun wallShape(mask: Int, tileSize: Float): Path {
    val quadrants = listOf(
        quadrantPath(-1f, -1f, mask.hasBit(Blob.W), mask.hasBit(Blob.N), mask.hasBit(Blob.NW), tileSize),
        quadrantPath(1f, -1f, mask.hasBit(Blob.E), mask.hasBit(Blob.N), mask.hasBit(Blob.NE), tileSize),
        quadrantPath(1f, 1f, mask.hasBit(Blob.E), mask.hasBit(Blob.S), mask.hasBit(Blob.SE), tileSize),
        quadrantPath(-1f, 1f, mask.hasBit(Blob.W), mask.hasBit(Blob.S), mask.hasBit(Blob.SW), tileSize),
    )
    return quadrants.reduce { shape, quadrant ->
        Path.combine(PathOperation.Union, shape, quadrant)
    }
}

private fun quadrantPath(
    signX: Float,
    signY: Float,
    horizontal: Boolean,
    vertical: Boolean,
    diagonal: Boolean,
    tileSize: Float,
): Path {
    val path = Path()
    quadrantOutline(horizontal, vertical, diagonal).forEachIndexed { index, point ->
        val x = (0.5f + signX * point.x) * tileSize
        val y = (0.5f + signY * point.y) * tileSize
        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

/** Outline of one quadrant, measured outwards from the tile's centre. */
private fun quadrantOutline(horizontal: Boolean, vertical: Boolean, diagonal: Boolean): List<Offset> {
    val start = -QUADRANT_OVERLAP
    val edge = 0.5f - WALL_MARGIN
    val far = 0.5f + OVERHANG

    return when {
        horizontal && vertical && diagonal -> rectangle(start, far, far)
        horizontal && vertical -> innerCorner(start, edge, far)
        horizontal -> rectangle(start, far, edge)
        vertical -> rectangle(start, edge, far)
        else -> outerCorner(start, edge)
    }
}

private fun rectangle(start: Float, right: Float, bottom: Float) = listOf(
    Offset(start, start),
    Offset(right, start),
    Offset(right, bottom),
    Offset(start, bottom),
)

private fun outerCorner(start: Float, edge: Float): List<Offset> {
    val center = edge - OUTER_RADIUS
    val arc = arcAngles().map { angle ->
        Offset(center + OUTER_RADIUS * cos(angle), center + OUTER_RADIUS * sin(angle))
    }
    return listOf(Offset(start, start), Offset(edge, start)) + arc + Offset(start, edge)
}

/** The radius has to equal the margin, so the curve ends exactly on the tile's border. */
private fun innerCorner(start: Float, edge: Float, far: Float): List<Offset> {
    val center = edge + WALL_MARGIN
    val arc = arcAngles().map { angle ->
        Offset(center - WALL_MARGIN * sin(angle), center - WALL_MARGIN * cos(angle))
    }
    return listOf(Offset(start, start), Offset(far, start), Offset(far, edge)) +
        arc + listOf(Offset(edge, far), Offset(start, far))
}

private fun arcAngles(): List<Float> =
    (0..ARC_STEPS).map { step -> step.toFloat() / ARC_STEPS * (PI.toFloat() / 2f) }

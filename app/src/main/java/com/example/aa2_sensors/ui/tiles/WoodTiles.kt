package com.example.aa2_sensors.ui.tiles

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
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

internal val ShadowLayer = Color(0x1E000000)
private val BevelLight = Color(0x2EFFD9A0)
private val BevelDark = Color(0x33200800)

/** Wider than the ball, and with the lip it still ends on the tile's border. */
private const val HOLE_RADIUS = 0.44f

private const val HOLE_SHADOW_STEPS = 6
private const val HOLE_SHADOW_STEP_SIZE = 0.025f

/** Light enough for the inner shadow to show up on it. */
private val HoleFloor = Color(0xFF3A2212)
private val HoleShadow = Color(0x4D000000)

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
 * The map's top left corner sits at [origin], and walls carry on past the map until they
 * fill the whole draw area.
 * Keep [tileSize] and [origin] whole numbers of pixels, otherwise hairline gaps show between tiles.
 */
fun DrawScope.drawWoodMap(
    rows: List<String>,
    tileSize: Float,
    origin: Offset = Offset.Zero,
    wall: Char = 'w',
) {
    val columns = rows.maxOf { it.length }

    // Off-map counts as wall, so the map's outer edge has no bevel
    fun isWall(x: Int, y: Int): Boolean {
        if (y !in rows.indices || x !in 0 until columns) return true
        return rows[y].getOrNull(x) == wall
    }

    val firstX = floor(-origin.x / tileSize).toInt()
    val firstY = floor(-origin.y / tileSize).toInt()
    val endX = ceil((size.width - origin.x) / tileSize).toInt()
    val endY = ceil((size.height - origin.y) / tileSize).toInt()

    for (y in firstY until endY) {
        for (x in firstX until endX) {
            val topLeft = origin + Offset(x * tileSize, y * tileSize)
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
    // Fully enclosed tiles have no visible edge, so the outline work would all be hidden
    if (mask == Blob.ALL) {
        translate(topLeft.x, topLeft.y) {
            clipRect(0f, 0f, tileSize, tileSize) {
                drawWood(WallWood, worldX, worldY, tileSize)
            }
        }
        return
    }

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

/** Cuts a round hole into the floor. Draw it over a floor tile. */
fun DrawScope.drawWoodHole(tileSize: Float, topLeft: Offset = Offset.Zero) {
    val tileCenter = Offset(0.5f * tileSize, 0.5f * tileSize)
    val radius = HOLE_RADIUS * tileSize
    val hole = Path().apply { addOval(Rect(tileCenter, radius)) }

    translate(topLeft.x, topLeft.y) {
        drawHoleLip(tileCenter, radius, tileSize)
        drawCircle(color = HoleFloor, radius = radius, center = tileCenter)
        drawHoleShadow(hole, tileSize)
    }
}

/** Stacked rings overlap the most next to the hole, so the floor darkens evenly as it slopes in. */
private fun DrawScope.drawHoleLip(tileCenter: Offset, radius: Float, tileSize: Float) {
    val step = BEVEL_STEP_SIZE * tileSize
    for (layer in 1..BEVEL_STEPS) {
        drawCircle(color = BevelDark, radius = radius + layer * step, center = tileCenter)
    }
}

/** The light comes from the top left, so the rim on that side shades the inside of the hole. */
private fun DrawScope.drawHoleShadow(hole: Path, tileSize: Float) {
    val step = HOLE_SHADOW_STEP_SIZE * tileSize
    for (layer in 1..HOLE_SHADOW_STEPS) {
        val offset = layer * step
        val shadow = Path.combine(PathOperation.Difference, hole, hole.shifted(offset, offset))
        drawPath(shadow, color = HoleShadow)
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

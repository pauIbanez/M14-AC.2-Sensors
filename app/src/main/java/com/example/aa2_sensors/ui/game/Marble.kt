package com.example.aa2_sensors.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import com.example.aa2_sensors.game.Ball
import com.example.aa2_sensors.game.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 *
 * COMPLETLEY AI GENERATED
 */


private const val SCRATCH_COUNT = 24
private const val SCRATCH_MIN_LENGTH = 0.2f
private const val SCRATCH_MAX_LENGTH = 0.6f
private const val SCRATCH_WIDTH = 0.04f

/** How far from the ball's edge scratches reach full strength, as the depth of their lowest end. */
private const val SCRATCH_EDGE_FADE = 0.35f

/** Fixed seed so every marble gets the same wear. */
private const val SCRATCH_SEED = 5

/** How close to a scratch the light has to be for it to glint, as a fraction of the ball's radius. */
private const val GLINT_RADIUS = 0.55f

/** Where the light hits a level ball, as a fraction of its radius on each axis. */
private const val HIGHLIGHT_POSITION = -0.38f
private const val HIGHLIGHT_RADIUS = 0.15f

/** How far the highlight slides when the phone is tilted all the way, as a fraction of the ball's radius. */
private const val HIGHLIGHT_TILT_RANGE = 0.5f

/** Keeps the highlight away from the ball's edge. */
private const val HIGHLIGHT_MAX_DISTANCE = 0.75f

/** How quickly the highlight catches up with the tilt. The sensor is too slow and noisy to follow directly. */
private const val HIGHLIGHT_RESPONSE = 10f

private const val GRAVITY = 9.81f

/** How far the ball's shadow falls towards the bottom right, as a fraction of its radius. */
private const val SHADOW_OFFSET = 0.22f
private const val SHADOW_RADIUS = 1.15f

/** How much of its size the ball has left when the shadow has fully gone. */
private const val SHADOW_GONE_AT = 0.6f

private val Steel = Color(0xFF98A2AC)
private val ScratchDark = Color(0x8C3A424A)
private val ScratchLight = Color(0xB3E6ECF2)
private val FloorReflection = Color(0x66A8733A)
private val FormShade = Color(0x99101418)
private val RimShade = Color(0x66000000)
private val Glint = Color(0xFFFFFFFF)
private val Highlight = Color(0xFFFFFFFF)
private val HighlightSoft = Color(0xCCFFFFFF)
private val FloorShadow = Color(0x66000000)

/** A point on a sphere of radius 1: x goes right, y goes down and z comes out of the screen. */
class SpherePoint(var x: Float, var y: Float, var z: Float) {

    /** Turns the point the way the ball's top moves when rolling along the unit direction. */
    fun roll(directionX: Float, directionY: Float, cosAngle: Float, sinAngle: Float) {
        val along = x * directionX + y * directionY
        val rolledAlong = along * cosAngle + z * sinAngle
        z = z * cosAngle - along * sinAngle
        x += (rolledAlong - along) * directionX
        y += (rolledAlong - along) * directionY
    }
}

class Scratch(val start: SpherePoint, val end: SpherePoint, val color: Color)

/**
 * The look of the ball. Only the scratches turn as it rolls: on polished metal the shading
 * comes from the light, so it only moves when the board tilts under that light.
 */
class Marble {
    val scratches: List<Scratch> = Random(SCRATCH_SEED).let { random ->
        List(SCRATCH_COUNT) { index -> randomScratch(random, index) }
    }

    /** Where the light hits the ball, as a fraction of its radius. */
    var lightSpot by mutableStateOf(Offset(HIGHLIGHT_POSITION, HIGHLIGHT_POSITION)); private set

    private var followed: Ball? = null
    private var lastX = 0f
    private var lastY = 0f

    /**
     * Eases the highlight towards the raw accelerometer [tilt], using the same axes as the ball.
     * The light stays overhead, so its reflection slides towards the high side of the board.
     */
    fun updateLight(tilt: Vector2, dt: Float) {
        val resting = Offset(HIGHLIGHT_POSITION, HIGHLIGHT_POSITION)
        val target = resting - Offset(tilt.y, tilt.x) * (HIGHLIGHT_TILT_RANGE / GRAVITY)

        val distance = target.getDistance()
        val reachable =
            if (distance > HIGHLIGHT_MAX_DISTANCE) target * (HIGHLIGHT_MAX_DISTANCE / distance) else target

        lightSpot = lerp(lightSpot, reachable, 1f - exp(-HIGHLIGHT_RESPONSE * dt))
    }

    /** Rolls by however far the ball moved. A new ball starts somewhere else, so it doesn't count. */
    fun follow(ball: Ball) {
        val pos = ball.pos
        if (ball === followed) roll(pos.x - lastX, pos.y - lastY)
        followed = ball
        lastX = pos.x
        lastY = pos.y
    }

    private fun roll(deltaX: Float, deltaY: Float) {
        val distance = hypot(deltaX, deltaY)
        if (distance == 0f) return

        val directionX = deltaX / distance
        val directionY = deltaY / distance
        val angle = distance / Ball.RADIUS
        val cosAngle = cos(angle)
        val sinAngle = sin(angle)

        for (scratch in scratches) {
            scratch.start.roll(directionX, directionY, cosAngle, sinAngle)
            scratch.end.roll(directionX, directionY, cosAngle, sinAngle)
        }
    }
}

private fun randomScratch(random: Random, index: Int): Scratch {
    val start = randomSpherePoint(random)
    val pull = randomSpherePoint(random)
    val length = SCRATCH_MIN_LENGTH + random.nextFloat() * (SCRATCH_MAX_LENGTH - SCRATCH_MIN_LENGTH)

    val x = start.x + pull.x * length
    val y = start.y + pull.y * length
    val z = start.z + pull.z * length
    val magnitude = max(sqrt(x * x + y * y + z * z), 0.001f)
    val end = SpherePoint(x / magnitude, y / magnitude, z / magnitude)

    return Scratch(start, end, if (index % 2 == 0) ScratchDark else ScratchLight)
}

/** Picking the height evenly is what spreads the points evenly over a sphere. */
private fun randomSpherePoint(random: Random): SpherePoint {
    val z = random.nextFloat() * 2f - 1f
    val angle = random.nextFloat() * 2f * PI.toFloat()
    val ring = sqrt(1f - z * z)
    return SpherePoint(ring * cos(angle), ring * sin(angle), z)
}

fun DrawScope.drawMarble(marble: Marble, ball: Ball, tileSize: Float) {
    marble.follow(ball)

    val shrink = ball.drawnRadius / Ball.RADIUS
    if (shrink <= 0f) return

    val radius = Ball.RADIUS * tileSize
    val shadowAlpha = ((shrink - SHADOW_GONE_AT) / (1f - SHADOW_GONE_AT)).coerceIn(0f, 1f)

    translate(ball.pos.x * tileSize, ball.pos.y * tileSize) {
        scale(shrink, Offset.Zero) {
            drawFloorShadow(radius, shadowAlpha)
            drawCircle(color = Steel, radius = radius, center = Offset.Zero)
            drawScratches(marble.scratches, marble.lightSpot, radius)
            drawShading(marble.lightSpot * radius, radius)
        }
    }
}

private fun DrawScope.drawFloorShadow(radius: Float, alpha: Float) {
    val center = Offset(SHADOW_OFFSET * radius, SHADOW_OFFSET * radius)
    val shadowRadius = SHADOW_RADIUS * radius

    drawCircle(
        brush = Brush.radialGradient(
            0.55f to FloorShadow,
            1f to Color.Transparent,
            center = center,
            radius = shadowRadius,
        ),
        radius = shadowRadius,
        center = center,
        alpha = alpha,
    )
}

/**
 * Anything past the edge is on the back of the ball, and the ends fade out as they get there.
 * Scratches catch the light as they pass [lightSpot], which ties them to the highlight above.
 */
private fun DrawScope.drawScratches(scratches: List<Scratch>, lightSpot: Offset, radius: Float) {
    val width = max(1f, SCRATCH_WIDTH * radius)

    for (scratch in scratches) {
        val depth = min(scratch.start.z, scratch.end.z)
        if (depth <= 0f) continue

        val middle = Offset(
            (scratch.start.x + scratch.end.x) / 2f,
            (scratch.start.y + scratch.end.y) / 2f,
        )
        val closeness = 1f - (middle - lightSpot).getDistance() / GLINT_RADIUS
        val glint = closeness.coerceAtLeast(0f)

        drawLine(
            color = lerp(scratch.color, Glint, glint * glint),
            start = Offset(scratch.start.x * radius, scratch.start.y * radius),
            end = Offset(scratch.end.x * radius, scratch.end.y * radius),
            strokeWidth = width,
            cap = StrokeCap.Round,
            alpha = (depth / SCRATCH_EDGE_FADE).coerceAtMost(1f),
        )
    }
}

/** The part that never rolls: the wood reflected underneath, the ball's form and the highlight. */
private fun DrawScope.drawShading(lightSpot: Offset, radius: Float) {
    drawCircle(
        brush = Brush.linearGradient(
            0.5f to Color.Transparent,
            1f to FloorReflection,
            start = Offset(0f, -radius),
            end = Offset(0f, radius),
        ),
        radius = radius,
        center = Offset.Zero,
    )
    drawCircle(
        brush = Brush.radialGradient(
            0.55f to Color.Transparent,
            1f to FormShade,
            center = lightSpot,
            radius = radius * 1.5f,
        ),
        radius = radius,
        center = Offset.Zero,
    )
    drawCircle(
        brush = Brush.radialGradient(
            0.82f to Color.Transparent,
            1f to RimShade,
            center = Offset.Zero,
            radius = radius,
        ),
        radius = radius,
        center = Offset.Zero,
    )
    drawCircle(
        brush = Brush.radialGradient(
            0f to Highlight,
            0.35f to HighlightSoft,
            1f to Color.Transparent,
            center = lightSpot,
            radius = HIGHLIGHT_RADIUS * radius,
        ),
        radius = HIGHLIGHT_RADIUS * radius,
        center = lightSpot,
    )
}

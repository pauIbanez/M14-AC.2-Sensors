package com.example.aa2_sensors.game

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Vector2(var x: Float, var y: Float) {

    val sqrMagnitude: Float get() = x * x + y * y
    val magnitude: Float get() = sqrt(sqrMagnitude)

    /** Unit-length copy of this vector, or zero if this vector has no length. */
    val normalized: Vector2
        get() {
            val mag = magnitude
            return if (mag > 0f) this / mag else zero
        }

    operator fun minus(o: Vector2): Vector2 = Vector2(x - o.x, y - o.y)
    operator fun plus(o: Vector2): Vector2 = Vector2(x + o.x, y + o.y)
    operator fun times(o: Vector2): Vector2 = Vector2(x * o.x, y * o.y)
    operator fun div(o: Vector2): Vector2 = Vector2(x / o.x, y / o.y)

    operator fun times(s: Float): Vector2 = Vector2(x * s, y * s)
    operator fun div(s: Float): Vector2 = Vector2(x / s, y / s)

    operator fun unaryMinus(): Vector2 = Vector2(-x, -y)

    companion object {
        val zero: Vector2 get() = Vector2(0f, 0f)
        val one: Vector2 get() = Vector2(1f, 1f)
        val up: Vector2 get() = Vector2(0f, 1f)
        val down: Vector2 get() = Vector2(0f, -1f)
        val left: Vector2 get() = Vector2(-1f, 0f)
        val right: Vector2 get() = Vector2(1f, 0f)

        fun sqrMagnitude(v: Vector2): Float = v.sqrMagnitude
        fun magnitude(v: Vector2): Float = v.magnitude

        fun distance(a: Vector2, b: Vector2): Float = (b - a).magnitude
        fun sqrDistance(a: Vector2, b: Vector2): Float = (b - a).sqrMagnitude

        fun dot(a: Vector2, b: Vector2): Float = a.x * b.x + a.y * b.y

        /** Z component of the 3D cross product. Positive if b is counter-clockwise from a. */
        fun cross(a: Vector2, b: Vector2): Float = a.x * b.y - a.y * b.x

        /** Angle from a to b in radians, in the range -PI to PI. */
        fun signedAngle(a: Vector2, b: Vector2): Float = atan2(cross(a, b), dot(a, b))

        /** Unsigned angle between a and b in radians, in the range 0 to PI. */
        fun angle(a: Vector2, b: Vector2): Float = abs(signedAngle(a, b))

        /** Interpolates from a to b, with t clamped to the range 0 to 1. */
        fun lerp(a: Vector2, b: Vector2, t: Float): Vector2 = lerpUnclamped(a, b, t.coerceIn(0f, 1f))
        fun lerpUnclamped(a: Vector2, b: Vector2, t: Float): Vector2 = a + (b - a) * t

        /** Moves current towards target by at most maxDelta, without overshooting. */
        fun moveTowards(current: Vector2, target: Vector2, maxDelta: Float): Vector2 {
            val delta = target - current
            val dist = delta.magnitude
            return if (dist <= maxDelta || dist == 0f) target.copy() else current + delta / dist * maxDelta
        }

        /** Copy of v with its length limited to maxLength. */
        fun clampMagnitude(v: Vector2, maxLength: Float): Vector2 {
            return if (v.sqrMagnitude > maxLength * maxLength) v.normalized * maxLength else v.copy()
        }

        /** Reflects v off a surface with the given unit-length normal. */
        fun reflect(v: Vector2, normal: Vector2): Vector2 = v - normal * (2f * dot(v, normal))

        /** Projects v onto the direction of onto. Returns zero if onto has no length. */
        fun project(v: Vector2, onto: Vector2): Vector2 {
            val sqrMag = onto.sqrMagnitude
            return if (sqrMag > 0f) onto * (dot(v, onto) / sqrMag) else zero
        }

        fun min(a: Vector2, b: Vector2): Vector2 = Vector2(min(a.x, b.x), min(a.y, b.y))
        fun max(a: Vector2, b: Vector2): Vector2 = Vector2(max(a.x, b.x), max(a.y, b.y))
    }
}

operator fun Float.times(v: Vector2): Vector2 = v * this

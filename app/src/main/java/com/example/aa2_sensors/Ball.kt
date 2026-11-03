// package com.example.aa2_sensors

// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.setValue
// import androidx.compose.ui.geometry.Offset
// import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.graphics.drawscope.DrawScope
// import kotlin.math.abs
// import kotlin.math.ceil
// import kotlin.math.floor
// import kotlin.math.max
// import kotlin.math.sqrt

// class Ball(val radius: Float = 0.4f) {

//     var pos by mutableStateOf(Vector2(0f, 0f))
//     var isAlive by mutableStateOf(false)
//     var isDying = false
//     var drawnRadius by mutableStateOf(0f)
//     var deathAnimTime = 0f
//     val deathTime = 1f

//     var onDeath: () -> Unit = { TODO("Not implemented") }

//     init {
//         drawnRadius = radius
//     }

//     private var vel = Vector2(0f, 0f)

//     fun update(rot: Vector2, acceleration: Float, dt: Float, level: Level_old) {
//         if (!isAlive) {
//             if (!isDying) return
//             deathAnimTime += dt
//             if (deathAnimTime >= deathTime) {
//                 isDying = false
//                 drawnRadius = 0f
//                 onDeath()
//                 return
//             }
//             val prop = deathAnimTime / deathTime

//             val target = Vector2(pos.x.toInt().toFloat() + 0.5f, pos.y.toInt().toFloat() + 0.5f)
//             val ab = Vector2(target.x - pos.x, target.y - pos.y)
//             val abMag = sqrt(ab.x * ab.x + ab.y * ab.y)
//             val dir = Vector2(ab.x / abMag, ab.y / abMag)
//             pos = Vector2(pos.x + dir.x * 2f * dt, pos.y + dir.y * 2f * dt)
//             if (prop < 0.2f) return
//             drawnRadius = max(0f, drawnRadius - 0.6f * dt)
//             return
//         }

//         vel = Vector2(vel.x + rot.y * acceleration * dt, vel.y + rot.x * acceleration * dt)

//         // I had a "AABB-based" collision system (though point samplig vs tile map rather than bound-box pair)
//         // However AI suggested a crcle-vs-tile, obviously better, so i let it put it in

//         val distance = max(abs(vel.x), abs(vel.y)) * dt
//         val steps = ceil(distance / (radius * 0.5f)).toInt().coerceAtLeast(1)
//         val stepDt = dt / steps

//         var x = pos.x
//         var y = pos.y
//         repeat(steps) {
//             x += vel.x * stepDt
//             y += vel.y * stepDt

//             for (cy in floor(y - radius).toInt()..floor(y + radius).toInt()) {
//                 for (cx in floor(x - radius).toInt()..floor(x + radius).toInt()) {
//                     if (!level.isSolidAt(Vector2(cx + 0.5f, cy + 0.5f))) continue


//                     val closestX = x.coerceIn(cx.toFloat(), cx + 1f)
//                     val closestY = y.coerceIn(cy.toFloat(), cy + 1f)
//                     val dx = x - closestX
//                     val dy = y - closestY
//                     val dist = sqrt(dx * dx + dy * dy)
//                     if (dist >= radius || dist == 0f) continue


//                     val nx = dx / dist
//                     val ny = dy / dist
//                     x += nx * (radius - dist)
//                     y += ny * (radius - dist)


//                     val intoWall = vel.x * nx + vel.y * ny
//                     if (intoWall < 0) vel = Vector2(vel.x - intoWall * nx, vel.y - intoWall * ny)
//                 }
//             }
//         }

//         pos = Vector2(x, y)
//     }

//     fun draw(scope: DrawScope, level: Level_old) {
//         val scale = scope.size.width / level.width
//         scope.drawCircle(color = Color.Red, radius = drawnRadius * scale, center = Offset(pos.x * scale, pos.y * scale))
//     }

//     fun live() {
//         vel = Vector2(0f, 0f)
//         drawnRadius = radius
//         isAlive = true
//         isDying = false
//     }

//     fun die(onDeath: () -> Unit) {
//         this.onDeath = onDeath
//         vel = Vector2(0f, 0f)
//         deathAnimTime = 0f
//         isAlive = false
//         isDying = true
//     }
// }

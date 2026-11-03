package com.example.aa2_sensors

import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aa2_sensors.ui.theme.AA2_SensorsTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


data class Vector2(val x: Float, val y: Float)

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

    private val ball = Ball()
    private var level = Level_old(ball, onComplete = { onComplete() }, onDeath = { onDeath() })

    private val acceleration = 1f
    private var rot: Vector2 = Vector2(0f, 0f)


    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }


        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        level.start()

        enableEdgeToEdge()
        setContent {
            AA2_SensorsTheme {
                InGame(level, onTick = { dt -> onTick(dt) }, ball)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        rot = Vector2(sensorEvent.values[0], sensorEvent.values[1])
    }


    fun onTick(dt: Float) {
        level.update()
        ball.update(rot, acceleration, dt, level)
    }

    fun onComplete() {}

    fun onDeath() {
        level.start()
    }

}


@Composable
fun Laberinth(level: Level_old, onTick: (dt: Float) -> Unit, onDraw: DrawScope.() -> Unit) {

    LaunchedEffect(Unit) {
        var last = withFrameNanos { it }
        while (true) {
            withFrameNanos { now ->
                onTick((now - last) / 1_000_000_000f)
                last = now
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .aspectRatio(level.width / level.height)

    ) {
        onDraw()
    }
}

@Composable
fun Restart(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("RESTART")
    }
}

@Composable
fun Attempts(nOfAtt: String, modifier: Modifier = Modifier) {
    Text(
        nOfAtt,
        modifier = modifier.padding(10.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Black
    )
}

@Composable
fun InGame(level: Level_old, onTick: (dt: Float) -> Unit, ball: Ball) {
    Box(modifier = Modifier.fillMaxSize()) {
        Laberinth(
            level,
            onTick = onTick,
            onDraw = {
                level.draw(this)
                ball.draw(this, level)
            }
        )
        Restart(
            onClick = {
                level.restart()
            }
        )
        Attempts(level.attempts.toString(), Modifier.align(Alignment.TopEnd))
    }
}

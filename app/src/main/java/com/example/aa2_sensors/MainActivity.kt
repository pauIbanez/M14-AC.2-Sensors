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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.aa2_sensors.ui.theme.AA2_SensorsTheme


data class Vector2(val x: Float, val y: Float)

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

    private var level = Level()
    private val ball = Ball(level.start)

    private val acceleration = 1f
    private var rot: Vector2 = Vector2(0f, 0f)

    // private var collisionMap = emptyArray<Array<Boolean>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)


        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        enableEdgeToEdge()
        setContent {
            AA2_SensorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Laberinth(
                            level,
                            onTick = { dt -> onTick(dt) },
                            onEnd = { onEnd() },
                            onDraw = {
                                level.draw(this)
                                ball.draw(this, level)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        rot = Vector2(sensorEvent.values[0], sensorEvent.values[1])
    }


    fun onTick(dt: Float) {
        ball.update(rot, acceleration, dt, level)
    }

    fun onEnd() {}
}


@Composable
fun Laberinth(level: Level, onTick: (dt: Float) -> Unit, onEnd: () -> Unit, onDraw: DrawScope.() -> Unit) {

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
            .wrapContentSize()                  // center it
            .aspectRatio(level.width / level.height)

    ) {
        onDraw()
    }
}

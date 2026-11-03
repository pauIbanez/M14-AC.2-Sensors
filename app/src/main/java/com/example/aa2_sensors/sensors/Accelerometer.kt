package com.example.aa2_sensors.sensors

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.aa2_sensors.game.Vector2

class Accelerometer(private val context: Context) : SensorEventListener {
    private lateinit var sensorManager: SensorManager
    var rot: Vector2 = Vector2(0f, 0f); private set

    fun init() {
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        rot = Vector2(sensorEvent.values[0], sensorEvent.values[1])
    }
}

@Composable
fun rememberAccelerometer(): Accelerometer {
    val context = LocalContext.current
    val accelerometer = remember { Accelerometer(context) }

    DisposableEffect(accelerometer) {
        accelerometer.init()
        onDispose { accelerometer.stop() }
    }

    return accelerometer
}


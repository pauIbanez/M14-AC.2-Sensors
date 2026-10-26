package com.example.aa2_sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.aa2_sensors.ui.theme.AA2_SensorsTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

    private var x by mutableFloatStateOf(0f)
    private var y by mutableFloatStateOf(0f)
    private var z by mutableFloatStateOf(0f)

    private var xVel = 0f
    private var yVel = 0f
    private var zVel = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        enableEdgeToEdge()
        setContent {
            AA2_SensorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SimpleGame()
                    }

                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {


        x = sensorEvent.values[0]
        y = sensorEvent.values[1]
        z = sensorEvent.values[2]
    }


}

@Composable
fun SimpleGame() {
    var circleX by remember { mutableFloatStateOf(200f) }
    var speedX by remember { mutableFloatStateOf(0f) }

    var moveLeft by remember { mutableStateOf(false) }
    var moveRight by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) { // Bucle del juego: aproximadamente 60 FPS
            delay(16)
            speedX = 0f
            if (moveLeft) {
                speedX = -5f
            }
            if (moveRight) {
                speedX = 5f
            }
            circleX += speedX
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures { offset ->
                val w = size.width
                moveRight = false
                moveLeft = false
                if (offset.x < w / 3) {
                    moveLeft = true
                } else if (offset.x > 2 * w / 3) {
                    moveRight = true
                }
            }
        }
    ) {
        drawCircle(
            color = Color.Red, radius = 40f,
            center = Offset(circleX, size.height / 2)
        )
    }
}

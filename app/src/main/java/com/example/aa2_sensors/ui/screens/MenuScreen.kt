package com.example.aa2_sensors.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun MenuScreen(onPlay: () -> Unit) {
    Button(onClick = { onPlay() }) {
        Text("PLAY")
    }
}

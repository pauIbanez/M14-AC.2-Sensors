package com.example.aa2_sensors.ui.game

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Restart(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("RESTART")
    }
}

@Composable
fun Attempts(nOfAtt: Int, modifier: Modifier = Modifier) {
    Text(
        nOfAtt.toString(),
        modifier = modifier.padding(10.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Black
    )
}


@Composable
fun GoBack(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) {
        Text("HOME")
    }
}

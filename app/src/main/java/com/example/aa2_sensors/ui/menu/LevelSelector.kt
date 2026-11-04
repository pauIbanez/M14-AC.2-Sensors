package com.example.aa2_sensors.ui.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LevelSelector(nOfLevels: Int, onLevelClick: (Int) -> Unit, onBack: () -> Unit) {
    Column {
        for (id in 1..nOfLevels) {
            LevelButton(id, onClick = { onLevelClick(id) })
        }
        Button(onClick = onBack) { Text("BACK") }
    }
}

@Composable
fun LevelButton(id: Int, onClick: () -> Unit) {
    Button(onClick = onClick) { Text("Level $id") }
}

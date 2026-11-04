package com.example.aa2_sensors.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.aa2_sensors.R
import com.example.aa2_sensors.ui.menu.LevelSelector


@Composable
fun MenuScreen(onLevelClick: (Int) -> Unit) {
    var showLevels by remember { mutableStateOf(false) }

    if (showLevels) {
        LevelSelector(
            nOfLevels = 6,
            onLevelClick = onLevelClick,
            onBack = { showLevels = false }
        )
    } else {
        Image(
            painter = painterResource(R.drawable.btn_play),
            contentDescription = "Play",
            modifier = Modifier.clickable { showLevels = true }
        )
    }
}

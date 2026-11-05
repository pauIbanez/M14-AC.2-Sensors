package com.example.aa2_sensors.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aa2_sensors.R
import com.example.aa2_sensors.ui.theme.LocalButtonSizes

@Composable
fun LevelSelectorScreen(onLevelClick: (Int) -> Unit, onBack: () -> Unit) {
    val sizes = LocalButtonSizes.current

    Box(modifier = Modifier.fillMaxSize()) {
        WoodBackground()

        Image(
            painter = painterResource(R.drawable.btn_close),
            contentDescription = "Quit",
            modifier = Modifier.size(sizes.button).align(Alignment.TopStart).padding(10.dp).clickable {
                onBack()
            },

        )

        WithTitle("LEVEL SELECTOR", modifier = Modifier.align(Alignment.Center)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(sizes.gap),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sizes.gap),
                ) {
                    for (id in 1..3) {
                        LevelButton(id, onClick = { onLevelClick(id) })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sizes.gap)
                ) {
                    for (id in 4..6) {
                        LevelButton(id, onClick = { onLevelClick(id) })
                    }
                }
            }
        }
    }
}

@Composable
fun LevelButton(id: Int, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.btn_blank),
            contentDescription = "Level $id",
            modifier = Modifier.size(LocalButtonSizes.current.button).clickable {
                onClick()
            },
        )
        Text(
            id.toString(), textAlign = TextAlign.Center,
            fontWeight = FontWeight.Black,
            fontSize = 30.sp,
            color = Color(0xFF571A00)
        )
    }
}

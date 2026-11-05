package com.example.aa2_sensors.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.aa2_sensors.R
import com.example.aa2_sensors.ui.menu.LevelSelector
import com.example.aa2_sensors.ui.menu.WoodBackground
import com.example.aa2_sensors.ui.theme.LocalButtonSizes


@Composable
fun MenuScreen(onLevelClick: (Int) -> Unit) {
    var showLevels by remember { mutableStateOf(false) }
    val (buttonSize, playButtonSize, buttonGap) = LocalButtonSizes.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        WoodBackground()

        if (showLevels) {
            LevelSelector(
                nOfLevels = 6,
                onLevelClick = onLevelClick,
                onBack = { showLevels = false }
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(buttonGap),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.btn_long),
                        contentDescription = "Title",
                        modifier = Modifier.height(buttonSize)
                    )
                    Text(
                        "LABERINTH", textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black,
                        fontSize = 30.sp,
                        color = Color(0xFF571A00)
                    )
                }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(buttonGap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.btn_close),
                        contentDescription = "Quit",
                        modifier = Modifier.size(buttonSize).clickable {
                            //TODO: Quit app
                        },
                    )
                    Image(
                        painter = painterResource(R.drawable.btn_play),
                        contentDescription = "Play",
                        modifier = Modifier.size(playButtonSize).clickable { onLevelClick(1) },

                    )
                    Image(
                        painter = painterResource(R.drawable.btn_list),
                        contentDescription = "List levels",
                        modifier = Modifier.size(buttonSize).clickable {
                            showLevels = true
                        },
                    )
                }
            }
        }
    }
}

package com.example.aa2_sensors.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.aa2_sensors.R
import com.example.aa2_sensors.ui.elements.WithTitle
import com.example.aa2_sensors.ui.menu.WoodBackground
import com.example.aa2_sensors.ui.theme.LocalButtonSizes


@Composable
fun MenuScreen(onPlay: () -> Unit, onShowLevels: () -> Unit) {
    val (buttonSize, playButtonSize, buttonGap) = LocalButtonSizes.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        WoodBackground()


        WithTitle("LABERINTH") {
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
                    modifier = Modifier.size(playButtonSize).clickable { onPlay() },

                )
                Image(
                    painter = painterResource(R.drawable.btn_list),
                    contentDescription = "List levels",
                    modifier = Modifier.size(buttonSize).clickable { onShowLevels() },
                )
            }
        }


    }
}

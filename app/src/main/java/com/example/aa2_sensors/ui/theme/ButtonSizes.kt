package com.example.aa2_sensors.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp

data class ButtonSizes(val button: Dp, val playButton: Dp, val gap: Dp) {
    companion object {
        fun forHeight(height: Dp): ButtonSizes {
            val button = (height * 0.18f).coerceIn(56.dp, 96.dp)
            return ButtonSizes(button = button, playButton = button * 2, gap = button * 0.35f)
        }
    }
}

val LocalButtonSizes = staticCompositionLocalOf<ButtonSizes> {
    error("ButtonSizes not provided: wrap the UI in WithButtonSizes")
}

@Composable
fun WithButtonSizes(content: @Composable () -> Unit) {
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val height = with(LocalDensity.current) { windowHeight.toDp() }

    CompositionLocalProvider(LocalButtonSizes provides ButtonSizes.forHeight(height), content = content)
}

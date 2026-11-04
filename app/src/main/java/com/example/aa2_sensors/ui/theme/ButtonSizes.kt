package com.example.aa2_sensors.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp

data class ButtonSizes(val button: Dp, val playButton: Dp, val gap: Dp)

val LocalButtonSizes = staticCompositionLocalOf<ButtonSizes> {
    error("ButtonSizes not provided: wrap the UI in ProvideButtonSizes")
}

/** Works out the button sizes from the screen once, for everything inside [content]. */
@Composable
fun ProvideButtonSizes(content: @Composable () -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val button = (maxHeight * 0.18f).coerceIn(56.dp, 96.dp)
        val sizes = ButtonSizes(button = button, playButton = button * 2, gap = button * 0.35f)

        CompositionLocalProvider(LocalButtonSizes provides sizes, content = content)
    }
}

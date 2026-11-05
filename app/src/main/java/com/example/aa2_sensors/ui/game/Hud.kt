package com.example.aa2_sensors.ui.game

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aa2_sensors.ui.elements.ImageButton
import com.example.aa2_sensors.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


// @Composable
// fun Restart(onClick: () -> Unit) {
//     ImageButton(R.drawable.btn_pause, "Restart", onClick) // TODO: Change sprite
// }

@Composable
fun BoxScope.Attempts(nOfAtt: Int) {
    Text(
        nOfAtt.toString(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
    )
}


@Composable
fun BoxScope.GoBack(onClick: () -> Unit) {
    HomeButton(onClick, Modifier.align(Alignment.TopStart).padding(10.dp))
}

@Composable
fun HomeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ImageButton(R.drawable.btn_close, "Home", onClick, modifier) // TODO: Change sprite
}

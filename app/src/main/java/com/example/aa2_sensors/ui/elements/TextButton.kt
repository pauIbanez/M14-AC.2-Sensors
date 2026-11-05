package com.example.aa2_sensors.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.aa2_sensors.R
import com.example.aa2_sensors.ui.theme.LocalButtonSizes

@Composable
fun TextButton(text: String, onClick: () -> Unit, description: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.btn_blank),
            contentDescription = description,
            modifier = Modifier.size(LocalButtonSizes.current.button).clickable {
                onClick()
            },
        )
        Text(
            text, textAlign = TextAlign.Center,
            fontWeight = FontWeight.Black,
            fontSize = 30.sp,
            color = Color(0xFF571A00)
        )
    }
}

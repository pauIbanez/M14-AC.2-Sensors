package com.example.aa2_sensors.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aa2_sensors.R
import com.example.aa2_sensors.ui.theme.LocalButtonSizes


@Composable
fun WithTitle(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val (buttonSize, playButtonSize, buttonGap) = LocalButtonSizes.current
    Column(
        verticalArrangement = Arrangement.spacedBy(buttonGap),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(buttonSize).widthIn(min = buttonSize * 3),
        ) {
            Image(
                painter = painterResource(R.drawable.btn_long),
                contentDescription = "Title",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Text(
                title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                fontSize = 30.sp,
                color = Color(0xFF571A00),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        content()
    }
}

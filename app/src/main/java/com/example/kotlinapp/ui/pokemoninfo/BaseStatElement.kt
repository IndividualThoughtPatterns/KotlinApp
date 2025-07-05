package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.BaseStat

@Composable
fun BaseStatElement(modifier: Modifier, baseStat: BaseStat, color: Color) {
    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = baseStat.baseStatName,
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(40.dp),
            fontWeight = FontWeight.Bold,
            color = color
        )
        VerticalDivider()
        Text(text = baseStat.baseStatStringValue)
        LinearProgressIndicator(
            progress = { baseStat.baseStatValue.toFloat() / 233f },
            modifier = Modifier.widthIn(max = 210.dp),
            color = color,
            trackColor = color.copy(alpha = 0.24f),
            gapSize = 0.dp,
        )
    }
}
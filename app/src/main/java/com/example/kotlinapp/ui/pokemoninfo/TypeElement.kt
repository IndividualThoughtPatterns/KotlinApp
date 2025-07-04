package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TypeElement(text: String, color: Color) {
    Card(
        modifier = Modifier
            .padding(start = 15.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Text(
            text = text.replaceFirstChar { it.uppercase() },
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
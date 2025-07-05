package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.R

@Composable
fun WeightSection(modifier: Modifier) {
    Column(
        modifier = modifier.then(
            Modifier.fillMaxHeight()
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.weight),
                modifier = Modifier
                    .size(25.dp)
                    .padding(bottom = 10.dp, end = 10.dp),
                contentDescription = "weight image"
            )
            Text(
                text = "${(LocalPokemon.current.weight).toFloat() / 10} kg",
                modifier = Modifier
                    .padding(bottom = 10.dp),
            )
        }
        Text(
            text = "Weight",
            modifier = Modifier,
            color = Color.Gray,
            fontSize = 11.sp,
        )
    }
}
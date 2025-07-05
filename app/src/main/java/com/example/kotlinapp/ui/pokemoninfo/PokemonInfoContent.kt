package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonInfoContent(modifier: Modifier) {
    with(LocalPokemon.current) {
        val mainColor = colorResource(getColor(types[0]))

        Box(
            modifier = modifier.then(
                Modifier
                    .fillMaxSize()
                    .background(mainColor)
            )
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.pokeball20),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(205.dp)
                    .padding(top = 8.dp, end = 8.dp),
                contentDescription = "Background pokeball"
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    // verticalAlignment = Alignment.Bottom // не работает
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 20.dp, top = 30.dp)
                ) {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Text(
                            text = name.replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold,
                            fontSize = 33.sp,
                            color = Color.White,
                        )
                    }
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Text(
                            text = "#" + get3digitValue(value = id),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                }
                AboutCard(
                    modifier = Modifier
                )
            }
            GlideImage(
                model = bigSprite,
                contentDescription = "pokemon avatar",
                modifier = Modifier
                    .size(220.dp)
                    .align(BiasAlignment(verticalBias = -0.75f, horizontalBias = 0f))
            )
        }
    }
}
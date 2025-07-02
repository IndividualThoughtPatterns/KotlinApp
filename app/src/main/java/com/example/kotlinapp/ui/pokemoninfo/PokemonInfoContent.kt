package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.R
import com.example.kotlinapp.data.Pokemon

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonInfoContent(pokemon: Pokemon) {
    with(pokemon) {
        val mainColor = colorResource(getColor(types[0]))
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor)
        ) {
            val (
                pokeballImageRef,
                pokemonInfoHeaderRef,
                avatarImageRef,
                aboutCardRef
            ) = createRefs()

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(pokemonInfoHeaderRef) {
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        top.linkTo(parent.top)
                    }
                    .padding(start = 30.dp, end = 20.dp, top = 30.dp)
            ) {
                val (pokemonNameRef, pokemonIdRef) = createRefs()
                Text(
                    text = name.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .constrainAs(pokemonNameRef) {
                            top.linkTo(parent.top)
                            absoluteLeft.linkTo(parent.absoluteLeft)
                        },
                    fontWeight = FontWeight.Bold,
                    fontSize = 33.sp,
                    color = Color.White
                )
                Text(
                    text = "#" + get3digitValue(value = id),
                    modifier = Modifier
                        .constrainAs(pokemonIdRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            absoluteRight.linkTo(parent.absoluteRight)
                        },
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.pokeball20),
                modifier = Modifier
                    .size(250.dp)
                    .constrainAs(pokeballImageRef) {
                        top.linkTo(parent.top)
                        absoluteRight.linkTo(parent.absoluteRight)
                    },
                contentDescription = "Background pokeball"
            )

            AboutCard(
                modifier = Modifier.constrainAs(aboutCardRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)

                    top.linkTo(parent.top)
                },
                pokemon = pokemon
            )

            GlideImage(
                model = bigSprite,
                contentDescription = "pokemon avatar",
                modifier = Modifier
                    .size(210.dp)
                    .constrainAs(avatarImageRef) {
                        centerTo(parent)
                        verticalBias = 0.15f
                    }
            )
        }
    }
}
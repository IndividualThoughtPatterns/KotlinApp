package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.kotlinapp.R
import com.example.kotlinapp.data.Pokemon

@Composable
fun HeightConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    ConstraintLayout(modifier = modifier) {
        val (
            heightImageRef,
            pokemonInfoHeightTextRef,
            heightLabelTextRef,
        ) = createRefs()

        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.straighten),
            modifier = Modifier
                .constrainAs(heightImageRef) {
                    bottom.linkTo(heightLabelTextRef.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(pokemonInfoHeightTextRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .size(25.dp)
                .padding(bottom = 10.dp, end = 10.dp),
            contentDescription = "height image"
        )
        Text(
            text = "${(pokemon.height).toFloat() / 10} m",
            modifier = Modifier
                .constrainAs(pokemonInfoHeightTextRef) {
                    bottom.linkTo(heightLabelTextRef.top)
                    absoluteLeft.linkTo(heightImageRef.absoluteRight)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top)
                }
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Height",
            modifier = Modifier
                .constrainAs(heightLabelTextRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

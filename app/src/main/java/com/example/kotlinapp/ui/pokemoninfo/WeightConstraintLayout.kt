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
fun WeightConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    ConstraintLayout(modifier = modifier) {
        val (
            weightImageRef,
            pokemonInfoWeightTextRef,
            weightLabelTextRef,
        ) = createRefs()

        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.weight),
            modifier = Modifier
                .constrainAs(weightImageRef) {
                    bottom.linkTo(weightLabelTextRef.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(pokemonInfoWeightTextRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .size(25.dp)
                .padding(bottom = 10.dp, end = 10.dp),
            contentDescription = "weight image"
        )
        Text(
            text = "${(pokemon.weight).toFloat() / 10} kg",
            modifier = Modifier
                .constrainAs(pokemonInfoWeightTextRef) {
                    bottom.linkTo(weightLabelTextRef.top)
                    absoluteLeft.linkTo(weightImageRef.absoluteRight)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top)
                }
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Weight",
            modifier = Modifier
                .constrainAs(weightLabelTextRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}
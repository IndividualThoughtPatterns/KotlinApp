package com.example.kotlinapp.ui.pokemoninfo

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.kotlinapp.data.Pokemon
import kotlin.text.replaceFirstChar

@Composable
fun AbilitiesConstraintLayout(modifier: Modifier, pokemon: Pokemon) {
    var abilityNames = ""
    for (i in pokemon.abilities.indices) {
        abilityNames += pokemon.abilities[i]
            .replaceFirstChar { it.uppercase() }
        if (i != pokemon.abilities.size - 1) abilityNames += "\n"
    }
    Log.d("mydebug", abilityNames)

    ConstraintLayout(modifier = modifier) {
        val (pokemonInfoAbilitiesTextRef, abilitiesLabelTextRef) = createRefs()

        Text(
            text = abilityNames,
            modifier = Modifier
                .constrainAs(pokemonInfoAbilitiesTextRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(abilitiesLabelTextRef.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Abilities",
            modifier = Modifier
                .constrainAs(abilitiesLabelTextRef) {
                    top.linkTo(pokemonInfoAbilitiesTextRef.bottom)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                },
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}
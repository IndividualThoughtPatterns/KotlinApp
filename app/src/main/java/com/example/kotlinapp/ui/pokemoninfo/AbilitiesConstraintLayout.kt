package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AbilitiesConstraintLayout(modifier: Modifier) {
    var abilityNames = ""

    with(LocalPokemon.current) {
        for (i in abilities.indices) {
            abilityNames += abilities[i]
                .replaceFirstChar { it.uppercase() }
            if (i != abilities.size - 1) abilityNames += "\n"
        }
    }

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
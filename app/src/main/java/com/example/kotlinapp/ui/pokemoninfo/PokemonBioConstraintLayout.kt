package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PokemonBioConstraintLayout(modifier: Modifier) {

    Row(
        modifier = modifier.then(
            Modifier.height(IntrinsicSize.Min) // без этого пиздец будет
        ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeightSection(modifier = Modifier.fillMaxHeight())
        VerticalDivider(modifier = Modifier)
        HeightSection(modifier = Modifier.fillMaxHeight())
        VerticalDivider(modifier = Modifier)
        AbilitiesSection(modifier = Modifier)
    }
}
package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbilitiesSection(modifier: Modifier) {
    var abilityNames = ""

    with(LocalPokemon.current) {
        for (i in abilities.indices) {
            abilityNames += abilities[i]
                .replaceFirstChar { it.uppercase() }
            if (i != abilities.size - 1) abilityNames += "\n"
        }
    }

    Column(
        modifier = modifier.then(
            Modifier.fillMaxHeight()
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = abilityNames,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        Text(
            text = "Abilities",
            modifier = Modifier,
            color = Color.Gray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}
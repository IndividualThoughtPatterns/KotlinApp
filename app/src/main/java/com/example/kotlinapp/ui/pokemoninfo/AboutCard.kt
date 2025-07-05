package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutCard(modifier: Modifier) {
    Card(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 160.dp,
                    bottom = 8.dp
                )
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        AboutCardContent(modifier = Modifier)
    }
}
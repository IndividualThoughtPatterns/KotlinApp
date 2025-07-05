package com.example.kotlinapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PokemonLoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier.then(
            Modifier.fillMaxSize()
        )
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
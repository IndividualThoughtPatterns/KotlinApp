package com.example.kotlinapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun PokemonLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp)
        )
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
package com.example.kotlinapp.ui.pokemonsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.kotlinapp.App

@Composable
fun PokemonSettingsScreen() {
    val sharedPreferencesRepository = App.instance.sharedPreferencesRepository
    val cachingMode by sharedPreferencesRepository.cachingModeFlow.collectAsState(initial = false)
    PokemonSettingsContent(
        state = cachingMode,
        onEvent = sharedPreferencesRepository::setCachingMode
    )
}
package com.example.kotlinapp.ui.pokemonlist

import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.PokemonItem

data class PokemonListScreenState(val loadingState: LoadingState<List<PokemonItem>>)

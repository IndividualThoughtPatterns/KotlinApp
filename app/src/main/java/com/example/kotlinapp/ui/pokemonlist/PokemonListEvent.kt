package com.example.kotlinapp.ui.pokemonlist

import com.example.kotlinapp.data.PokemonItem

interface PokemonListEvent {
    data class OnToggleFavoriteClick(val pokemonItem: PokemonItem) : PokemonListEvent
    data object OnScrolledBottom : PokemonListEvent
    data class OnPokemonItemClick(val name: String) : PokemonListEvent
}
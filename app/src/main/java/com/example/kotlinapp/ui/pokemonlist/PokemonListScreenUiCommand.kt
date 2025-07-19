package com.example.kotlinapp.ui.pokemonlist

sealed interface PokemonListScreenUiCommand {
    data class ShowErrorMessage(
        val message: String
    ) : PokemonListScreenUiCommand

    data class NavigateToPokemonInfo(
        val name: String
    ) : PokemonListScreenUiCommand

    data object NavigateToPokemonSettings : PokemonListScreenUiCommand
}
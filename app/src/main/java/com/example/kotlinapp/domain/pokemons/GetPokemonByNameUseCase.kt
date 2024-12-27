package com.example.kotlinapp.domain.pokemons

class GetPokemonByNameUseCase(
    private val pokemonRepository: PokemonRepository,
) {

    operator fun invoke(pokemonName: String) = pokemonRepository.getPokemonByName(pokemonName)
}
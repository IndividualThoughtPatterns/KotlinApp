package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinapp.App
import com.example.kotlinapp.domain.pokemons.GetPokemonByNameUseCase

class PokemonInfoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PokemonInfoViewModel(
            getPokemonByNameUseCase = GetPokemonByNameUseCase(
                pokemonRepository = App.instance.pokemonRepositoryImpl
            ),
            savedStateHandle = extras.createSavedStateHandle()
        ) as T
    }
}
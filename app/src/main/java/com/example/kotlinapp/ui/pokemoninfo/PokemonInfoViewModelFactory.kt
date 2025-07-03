package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class PokemonInfoViewModelFactory(val name: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PokemonInfoViewModel(name) as T
    }
}
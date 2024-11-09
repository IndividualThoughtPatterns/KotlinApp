package com.example.kotlinapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonInfoViewModelFactory(
    private val name: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonInfoViewModel(name) as T
    }
}
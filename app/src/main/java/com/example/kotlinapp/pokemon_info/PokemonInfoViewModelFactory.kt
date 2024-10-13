package com.example.kotlinapp.pokemon_info

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonInfoViewModelFactory(
    private val fragment: PokemonInfoFragment,
    val app: Application
) : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonInfoViewModel(fragment) as T
    }
}
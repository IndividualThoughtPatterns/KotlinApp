package com.example.kotlinapp.pokemon_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonListViewModelFactory(
    private val fragment: PokemonListFragment,
    val app: Application,
    private val handleNetworkError: () -> Unit
) : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonListViewModel(fragment, handleNetworkError) as T
    }
}
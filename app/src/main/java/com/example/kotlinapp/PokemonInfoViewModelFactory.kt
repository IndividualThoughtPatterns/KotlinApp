package com.example.kotlinapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonInfoViewModelFactory(
    private val name: String,
    val app: Application
) : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonInfoViewModel(name) as T
    }
}
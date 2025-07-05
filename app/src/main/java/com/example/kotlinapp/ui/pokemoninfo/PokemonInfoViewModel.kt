package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.App
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class PokemonInfoViewModel(val pokemonInfoName: String) : ViewModel() {
    private val _pokemonStateFlow = MutableStateFlow<Pokemon?>(null)
    val pokemonStateFlow = _pokemonStateFlow.asStateFlow()

    private val _loadingStateFlow = MutableStateFlow<LoadingState?>(null)
    val loadingStateFlow = _loadingStateFlow

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        _loadingStateFlow.update { LoadingState.STARTED }
        viewModelScope.launch {
            var pokemon: Pokemon? = null
            try {
                pokemon = App.instance.pokemonRepository.getPokemonByName(pokemonInfoName)
                _loadingStateFlow.update { LoadingState.SUCCESS }
            } catch (e: IOException) {
                e.printStackTrace()
                _loadingStateFlow.update { LoadingState.FAILED }
            }
            _pokemonStateFlow.update { pokemon }
        }
    }
}
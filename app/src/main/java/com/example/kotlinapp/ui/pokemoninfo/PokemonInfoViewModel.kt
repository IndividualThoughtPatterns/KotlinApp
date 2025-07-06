package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.App
import com.example.kotlinapp.data.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class PokemonInfoViewModel(val pokemonInfoName: String) : ViewModel() {
    private val _state = MutableStateFlow<PokemonInfoScreenState>(
        PokemonInfoScreenState(loadingState = LoadingState.STARTED)
    )
    val state = _state.asStateFlow()

    init {
        loadPokemon()
    }

    fun onEvent(event: PokemonInfoEvent) {
        when (event) {
            PokemonInfoEvent.OnRetryClick -> {
                loadPokemon()
            }
        }
    }

    fun loadPokemon() {
        _state.update { PokemonInfoScreenState(loadingState = LoadingState.STARTED) }

        viewModelScope.launch {
            try {
                _state.update {
                    PokemonInfoScreenState(
                        pokemon = App.instance.pokemonRepository.getPokemonByName(pokemonInfoName),
                        loadingState = LoadingState.SUCCESS
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
                _state.update { PokemonInfoScreenState(loadingState = LoadingState.FAILED) }
            }
        }
    }
}
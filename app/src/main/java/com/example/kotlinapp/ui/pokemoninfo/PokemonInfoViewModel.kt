package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.LoadingState.Loaded
import com.example.kotlinapp.data.LoadingState.Loading
import com.example.kotlinapp.data.source.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class PokemonInfoViewModel(val pokemonRepository: PokemonRepository, val pokemonInfoName: String) :
    ViewModel() {
    private val _state = MutableStateFlow<PokemonInfoScreenState>(
        PokemonInfoScreenState(loadingState = Loading)
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
        _state.update { PokemonInfoScreenState(loadingState = Loading) }

        viewModelScope.launch {
            try {
                _state.update {
                    PokemonInfoScreenState(
                        loadingState = Loaded(
                            value = pokemonRepository.getPokemonByName(
                                pokemonInfoName
                            )
                        )
                    )

                }
            } catch (e: IOException) {
                e.printStackTrace()
                _state.update { PokemonInfoScreenState(loadingState = LoadingState.Error(e)) }
            }
        }
    }
}
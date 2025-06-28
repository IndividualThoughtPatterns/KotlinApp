package com.example.kotlinapp.ui.pokemoninfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.kotlinapp.App
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import com.example.kotlinapp.domain.GetPokemonByNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonInfoViewModel(name: String) : ViewModel() {
    private val _pokemonStateFlow = MutableStateFlow<Pokemon?>(null)
    val pokemonStateFlow = _pokemonStateFlow.asStateFlow()

    private val _pokemonLoadingState = MutableStateFlow<LoadingState?>(null)
    val pokemonLoadingState = _pokemonLoadingState.asStateFlow()

    //    private val pokemonInfoRoute = savedStateHandle.toRoute<PokemonInfo>()
    private val pokemonInfoName = name

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            val pokemonWithLoadingState = GetPokemonByNameUseCase(
                pokemonRepository = App.instance.pokemonRepository,
                pokemonName = pokemonInfoName.toString()
            )()

            with(pokemonWithLoadingState) {
                _pokemonStateFlow.update { pokemon }
                _pokemonLoadingState.update { loadingState }
            }
        }
    }
}
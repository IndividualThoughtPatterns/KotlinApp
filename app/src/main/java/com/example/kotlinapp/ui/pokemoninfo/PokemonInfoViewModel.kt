package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.kotlinapp.App
import com.example.kotlinapp.domain.GetPokemonByNameUseCase
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import java.util.concurrent.Executors

class PokemonInfoViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private val _pokemonLiveData = MutableLiveData<Pokemon>()
    val pokemonLIveData: LiveData<Pokemon?> = _pokemonLiveData

    private val _pokemonLoadingState = MutableLiveData<LoadingState>()
    val pokemonLoadingState = _pokemonLoadingState

    private val pokemonInfoRoute = savedStateHandle.toRoute<PokemonInfo>()
    private val pokemonInfoName = pokemonInfoRoute.name

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        executor.submit {
            val pokemonWithLoadingState = GetPokemonByNameUseCase(
                pokemonRepository = App.instance.pokemonRepository,
                pokemonName = pokemonInfoName
            )()

            with(pokemonWithLoadingState) {
                _pokemonLiveData.postValue(pokemon)
                _pokemonLoadingState.postValue(loadingState)
            }
        }
    }
}
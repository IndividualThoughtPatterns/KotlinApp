package com.example.kotlinapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import java.io.IOException
import java.util.concurrent.Executors

class PokemonInfoViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private val pokemonRepository = App.instance.pokemonRepository
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
            try {
                _pokemonLiveData.postValue(pokemonRepository.getPokemonByName(pokemonInfoName))
                _pokemonLoadingState.postValue(
                    LoadingState(
                        isLoaded = true,
                        error = null
                    )
                )
            } catch (e: IOException) {
                _pokemonLoadingState.postValue(
                    LoadingState(
                        isLoaded = false,
                        error = e
                    )
                )
            }
        }
    }
}
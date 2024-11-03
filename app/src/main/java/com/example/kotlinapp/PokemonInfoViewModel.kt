package com.example.kotlinapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.util.concurrent.Executors

class PokemonInfoViewModel(val name: String) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private val pokemonRepository = App.instance.pokemonRepository
    private val _pokemonLiveData = MutableLiveData<Pokemon>()
    val pokemonLIveData: LiveData<Pokemon?> = _pokemonLiveData

    private val _pokemonLoadingState = MutableLiveData<LoadingState>()
    val pokemonLoadingState = _pokemonLoadingState

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        executor.submit {
            try {
                _pokemonLiveData.postValue(pokemonRepository.getPokemonByName(name))
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
package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.kotlinapp.ui.LoadingState
import com.example.kotlinapp.domain.pokemons.GetPokemonByNameUseCase
import com.example.kotlinapp.domain.pokemons.Pokemon
import java.io.IOException
import java.util.concurrent.Executors

class PokemonInfoViewModel(
    private val getPokemonByNameUseCase: GetPokemonByNameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private val _pokemonRemoteLiveData = MutableLiveData<Pokemon>()
    val pokemonRemoteLiveData: LiveData<Pokemon?> = _pokemonRemoteLiveData

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
                _pokemonRemoteLiveData.postValue(getPokemonByNameUseCase(pokemonInfoName))
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
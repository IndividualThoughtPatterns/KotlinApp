package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.App
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.LoadingState.Loaded
import com.example.kotlinapp.data.LoadingState.Loading
import com.example.kotlinapp.data.toPokemon
import com.example.kotlinapp.data.toPokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class PokemonInfoViewModel(val pokemonInfoName: String) : ViewModel() {
    private val sharedPreferencesRepository = App.instance.sharedPreferencesRepository
    private val cachingModeFlow = sharedPreferencesRepository.cachingModeFlow

    private val pokemonDao = App.instance.db.pokemonDao()

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

        viewModelScope.launch(context = Dispatchers.IO) {
            cachingModeFlow.collect { cachingEnabled ->
                if (cachingEnabled) {
                    loadFromCache()
                } else {
                    loadFromNetworkOnly()
                }
            }
        }
    }

    suspend fun loadFromCache() {
        pokemonDao.getPokemonByNameAsFlow(name = pokemonInfoName).collect { pokemonEntity ->
            if (pokemonEntity != null) {
                _state.update {
                    PokemonInfoScreenState(
                        loadingState = Loaded(
                            value = pokemonEntity.toPokemon()
                        )
                    )
                }
            } else {
                try {
                    pokemonDao.insert(
                        App.instance.pokemonRepository.getPokemonByName(
                            pokemonInfoName
                        ).toPokemonEntity()
                    )
                    loadFromCache()
                } catch (e: IOException) {
                    e.printStackTrace()
                    _state.update { PokemonInfoScreenState(loadingState = LoadingState.Error(e)) }
                }
            }
        }
    }

    suspend fun loadFromNetworkOnly() {
        try {
            _state.update {
                PokemonInfoScreenState(
                    loadingState = Loaded(
                        value = App.instance.pokemonRepository.getPokemonByName(
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
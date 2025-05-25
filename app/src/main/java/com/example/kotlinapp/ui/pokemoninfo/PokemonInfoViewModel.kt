package com.example.kotlinapp.ui.pokemoninfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.kotlinapp.App
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.Pokemon
import com.example.kotlinapp.domain.GetPokemonByNameUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonInfoViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    //private val executor = Executors.newSingleThreadExecutor()
    //private val _pokemonLiveData = MutableLiveData<Pokemon>()
    //val pokemonLIveData: LiveData<Pokemon?> = _pokemonLiveData
    private val coroutineScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO) // для теста, потом на ViewModelScope замению
    private val _pokemonStateFlow = MutableStateFlow<Pokemon?>(null)
    val pokemonStateFlow = _pokemonStateFlow.asStateFlow()

//    private val _pokemonLoadingState = MutableLiveData<LoadingState>()
//    val pokemonLoadingState = _pokemonLoadingState
    private val _pokemonLoadingState = MutableStateFlow<LoadingState?>(null)
    val pokemonLoadingState = _pokemonLoadingState.asStateFlow()

    private val pokemonInfoRoute = savedStateHandle.toRoute<PokemonInfo>()
    private val pokemonInfoName = pokemonInfoRoute.name

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        coroutineScope.launch {
            val pokemonWithLoadingState = GetPokemonByNameUseCase(
                pokemonRepository = App.instance.pokemonRepository,
                pokemonName = pokemonInfoName
            )()

            with(pokemonWithLoadingState) {
                //_pokemonLiveData.postValue(pokemon)
                //_pokemonLoadingState.postValue(loadingState)
                _pokemonStateFlow.update { pokemon }
                _pokemonLoadingState.update { loadingState }
            }
        }
//        executor.submit {
//
//        }
    }
}
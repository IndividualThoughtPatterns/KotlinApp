package com.example.kotlinapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.util.concurrent.Executors

class PokemonInfoViewModel(name: String) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()
    private val pokemonRepository = App.instance.pokemonRepository
    private val _pokemonLiveData = MutableLiveData<Pokemon>()
    val pokemonLIveData: LiveData<Pokemon?> = _pokemonLiveData

    init {
        try {
            executor.submit {
                _pokemonLiveData.postValue(pokemonRepository.getPokemonByName(name))
            }
        } catch (e: IOException) {
            Log.d("pokemon info failure", e.message!!)
        }
    }
}
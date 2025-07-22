package com.example.kotlinapp.ui.pokemonlist

import androidx.paging.PagingData
import com.example.kotlinapp.data.PokemonItem
import kotlinx.coroutines.flow.Flow

data class PokemonListScreenState(val pokemonItemListPaging: Flow<PagingData<PokemonItem>>)

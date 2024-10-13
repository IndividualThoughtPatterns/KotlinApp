package com.example.kotlinapp

import android.app.Application
import androidx.room.Room
import com.example.kotlinapp.pokemon_database.AppDatabase
import com.example.kotlinapp.pokemon_info.PokemonInfoViewModel
import com.example.kotlinapp.pokemon_list.PokemonListViewModel
import com.example.kotlinapp.pokemon_network.PokemonNetwork

class App : Application() {
    val pokemonNetwork = PokemonNetwork()

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "favorite_pokemons.db"
        ).build()
    }

    companion object {
        lateinit var instance: App
        lateinit var db: AppDatabase
        lateinit var pokemonListViewModel: PokemonListViewModel
        lateinit var pokemonInfoViewModel: PokemonInfoViewModel
    }
}
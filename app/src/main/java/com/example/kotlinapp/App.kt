package com.example.kotlinapp

import android.app.Application
import androidx.room.Room
import com.example.kotlinapp.data.source.PokemonRepository
import com.example.kotlinapp.data.source.local.AppDatabase

class App : Application() {
    val pokemonRepository = PokemonRepository()
    lateinit var db: AppDatabase

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
    }
}
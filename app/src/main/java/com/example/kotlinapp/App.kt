package com.example.kotlinapp

import android.app.Application
import androidx.room.Room

class App : Application() {
    val pokemonNetwork = PokemonNetwork()
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
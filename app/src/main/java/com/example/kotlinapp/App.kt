package com.example.kotlinapp

import android.app.Application
import androidx.room.Room
import com.example.kotlinapp.data.source.PokemonRepository
import com.example.kotlinapp.data.source.local.AppDatabase
import com.example.kotlinapp.data.source.local.SharedPreferencesRepository

class App : Application() {
    val pokemonRepository = PokemonRepository()
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "favorite_pokemons.db"
        ).build()
        sharedPreferencesRepository = SharedPreferencesRepository(context = applicationContext)
    }

    companion object {
        lateinit var instance: App
    }
}
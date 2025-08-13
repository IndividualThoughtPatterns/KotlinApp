package com.example.kotlinapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    //    val pokemonRepository = PokemonRepository()
//    lateinit var db: AppDatabase
//
//    override fun onCreate() {
//        super.onCreate()
//        instance = this
//        db = Room.databaseBuilder(
//            this,
//            AppDatabase::class.java,
//            "favorite_pokemons.db"
//        ).build()
//    }
//
//    companion object {
//        lateinit var instance: App
//    }
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(appModule)
        }
    }
}
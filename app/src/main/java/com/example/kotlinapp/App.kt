package com.example.kotlinapp

import android.app.Application
import androidx.room.Room
import com.example.kotlinapp.data.source.PokemonRepositoryImpl
import com.example.kotlinapp.data.source.local.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class App : Application() {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()

    private val baseURL = "https://pokeapi.co/api/v2/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    lateinit var db: AppDatabase
    val pokemonRepositoryImpl by lazy {
        PokemonRepositoryImpl(
            pokemonApi = retrofit.create(),
            favoritePokemonDao = db.favoritePokemonDao()
        )
    }

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


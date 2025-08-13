package com.example.kotlinapp

import androidx.room.Room
import com.example.kotlinapp.data.source.PokemonRepository
import com.example.kotlinapp.data.source.local.AppDatabase
import com.example.kotlinapp.data.source.remote.NetworkApiInterface
import com.example.kotlinapp.ui.pokemoninfo.PokemonInfoViewModel
import com.example.kotlinapp.ui.pokemonlist.PokemonListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single<okhttp3.Interceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single {
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor = get())
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()

        val baseURL = "https://pokeapi.co/api/v2/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(NetworkApiInterface::class.java)
    }

    single {
        PokemonRepository(networkApiInterface = get())
    }

    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "favorite_pokemons.db"
        ).build()
    }
    single {
        val db: AppDatabase = get()
        db.favoritePokemonDao()
    }

    viewModel<PokemonListViewModel> {
        PokemonListViewModel(pokemonRepository = get(), favoritePokemonDao = get())
    }

    factory { (pokemonInfoName: String) -> // не уверен, что это правильный способ
        PokemonInfoViewModel(pokemonRepository = get(), pokemonInfoName = pokemonInfoName)
    }
}
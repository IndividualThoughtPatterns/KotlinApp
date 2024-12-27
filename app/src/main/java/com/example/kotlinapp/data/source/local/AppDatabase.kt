package com.example.kotlinapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinapp.data.source.local.favorites.FavoritePokemon
import com.example.kotlinapp.data.source.local.favorites.FavoritePokemonDao

@Database(entities = [FavoritePokemon::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}
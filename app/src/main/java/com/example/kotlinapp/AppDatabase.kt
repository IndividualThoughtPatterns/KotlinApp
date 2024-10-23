package com.example.kotlinapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoritePokemon::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}
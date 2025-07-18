package com.example.kotlinapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinapp.data.FavoritePokemonEntity
import com.example.kotlinapp.data.PokemonEntity
import com.example.kotlinapp.data.PokemonItemWithIdEntity

@Database(
    entities = [
        FavoritePokemonEntity::class,
        PokemonItemWithIdEntity::class,
        PokemonEntity::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao
    abstract fun pokemonItemWithIdDao(): PokemonItemWithIdDao
    abstract fun pokemonDao(): PokemonDao

    suspend fun getLastUpdated(): Long {
        return pokemonItemWithIdDao().getLastUpdatedTimestamp() ?: 0L
    }
}
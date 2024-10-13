package com.example.kotlinapp.pokemon_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritePokemonDao {
    @Query("SELECT * FROM favorite_pokemons")
    fun getAll(): LiveData<List<FavoritePokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoritePokemon: FavoritePokemon)

    @Query("DELETE FROM favorite_pokemons WHERE name=:name")
    fun deleteByName(name: String)
}
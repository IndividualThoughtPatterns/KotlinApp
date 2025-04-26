package com.example.kotlinapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinapp.data.FavoritePokemon

@Dao
interface FavoritePokemonDao {
    @Query("SELECT * FROM favorite_pokemons")
    fun getAll(): LiveData<List<FavoritePokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoritePokemon: FavoritePokemon)

    @Query("DELETE FROM favorite_pokemons WHERE name=:name")
    fun deleteByName(name: String)
}
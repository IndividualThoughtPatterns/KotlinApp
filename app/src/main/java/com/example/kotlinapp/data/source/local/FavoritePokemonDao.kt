package com.example.kotlinapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinapp.data.FavoritePokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePokemonDao {
    @Query("SELECT * FROM favorite_pokemons")
    fun getAllAsFlow(): Flow<List<FavoritePokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoritePokemon: FavoritePokemonEntity)

    @Query("DELETE FROM favorite_pokemons WHERE name=:name")
    fun deleteByName(name: String)
}
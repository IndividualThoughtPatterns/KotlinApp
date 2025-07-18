package com.example.kotlinapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinapp.data.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemons WHERE name = :name")
    fun getPokemonByNameAsFlow(name: String): Flow<PokemonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemonEntity: PokemonEntity)
}
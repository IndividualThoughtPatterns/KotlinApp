package com.example.kotlinapp.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinapp.data.PokemonItemWithIdEntity

@Dao
interface PokemonItemWithIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonItems: List<PokemonItemWithIdEntity>)

    @Query("SELECT * FROM pokemon_items")
    fun pagingSource(): PagingSource<Int, PokemonItemWithIdEntity>

    @Query("DELETE FROM pokemon_items")
    suspend fun clearAll()

    @Query("SELECT timestamp from pokemon_items WHERE id = 1")
    suspend fun getLastUpdatedTimestamp(): Long?
}
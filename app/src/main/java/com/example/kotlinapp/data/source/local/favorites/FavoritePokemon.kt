package com.example.kotlinapp.data.source.local.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemons")
data class FavoritePokemon(
    @ColumnInfo(name = "name")
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
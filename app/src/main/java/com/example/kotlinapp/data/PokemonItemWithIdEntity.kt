package com.example.kotlinapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_items")
data class PokemonItemWithIdEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "small_sprite") // надо будет разобраться с BLOB и сжатием
    val sprite: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long?
)
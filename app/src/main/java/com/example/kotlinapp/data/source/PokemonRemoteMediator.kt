package com.example.kotlinapp.data.source

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.kotlinapp.data.PokemonItemWithIdEntity
import com.example.kotlinapp.data.source.local.AppDatabase
import okio.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    val pokemonRepository: PokemonRepository,
    val database: AppDatabase
) : RemoteMediator<Int, PokemonItemWithIdEntity>() {
    val pokemonItemWithIdDao = database.pokemonItemWithIdDao()

    private val limit = 20
    private var offsetFactor = 0
    private var offset = limit * offsetFactor

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = database.getLastUpdated()
        return if (System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonItemWithIdEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    offsetFactor = 0
                    0
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    offsetFactor++
                    offset = limit * offsetFactor
                    offset
                }
            }

            Log.d("mydebug", state.pages.size.toString())

            Log.d("mydebug", "Load key (offset): $loadKey, offset: ($offset) LoadType: $loadType")

            val pokemonItemWithIdList =
                pokemonRepository.getPokemonList(limit = state.config.pageSize, offset = loadKey)

            val pokemonItemWithIdEntityList = pokemonItemWithIdList.map {
                PokemonItemWithIdEntity(
                    name = it.name,
                    sprite = it.smallSprite,
                    timestamp = System.currentTimeMillis()
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonItemWithIdDao.clearAll()
                }

                pokemonItemWithIdDao.insertAll(pokemonItemWithIdEntityList)
            }

            MediatorResult.Success(
                endOfPaginationReached = pokemonItemWithIdList.isEmpty()
            )
        } catch (e: IOException) {
            e.printStackTrace()
            MediatorResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}
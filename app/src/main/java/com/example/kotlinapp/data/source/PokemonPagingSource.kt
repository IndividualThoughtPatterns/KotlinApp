package com.example.kotlinapp.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PokemonPagingSource(
    private val dataSource: PokemonRepository
) : PagingSource<Int, PokemonRepository.PokemonItemWithId>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonRepository.PokemonItemWithId> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize
            val offset = page * limit

            val pokemonItemWithIdsList =
                dataSource.getPokemonList(limit, offset)
            LoadResult.Page(
                data = pokemonItemWithIdsList,
                prevKey = null,
                nextKey = if (pokemonItemWithIdsList.size < limit) null else page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonRepository.PokemonItemWithId>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
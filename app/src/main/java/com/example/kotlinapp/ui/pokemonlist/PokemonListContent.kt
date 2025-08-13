package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.PokemonLoadingScreen

@Composable
fun PokemonListContent(
    state: PokemonListScreenState,
    onEvent: (PokemonListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val pokemonListState = state.pokemonItemListPaging.collectAsLazyPagingItems()
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(count = pokemonListState.itemCount) { index ->
            val item = pokemonListState[index]!!
            PokemonElement(
                pokemonItem = item,
                onPokemonItemClick = {
                    onEvent(
                        PokemonListEvent.OnPokemonItemClick(
                            name = item.name
                        )
                    )
                },
                onToggleFavoriteClick = { pokemonItem: PokemonItem ->
                    onEvent(PokemonListEvent.OnToggleFavoriteClick(pokemonItem))
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        pokemonListState.apply {
            if (loadState.refresh is LoadState.Error || loadState.append is LoadState.Error) {
                item {
                    ErrorItem(onClickRetry = { retry() })
                }
            }
        }
    }
    pokemonListState.apply {
        if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
            PokemonLoadingScreen(modifier = Modifier)
        }
    }
}

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Ошибка сети",
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onClickRetry) {
            Text(text = "Загрузить")
        }
    }
}

//@Preview
//@Composable
//fun PokemonListContentPreview(
//    @PreviewParameter(PokemonListUiStateProvider::class) state: PokemonListScreenState
//) {
//    PokemonListContent(
//        state = state,
//        onEvent = {}
//    )
//}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonElement(
    pokemonItem: PokemonItem,
    onPokemonItemClick: () -> Unit,
    onToggleFavoriteClick: (pokemonItem: PokemonItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onPokemonItemClick()
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = pokemonItem.sprite,
                contentDescription = "sprite url",
                modifier = Modifier
                    .size(54.dp)
                    .padding(8.dp)
            )
            Text(
                pokemonItem.name.replaceFirstChar { it.uppercase() },
                modifier = Modifier
                    .weight(6f),
                style = MaterialTheme.typography.headlineSmall,
            )
            IconButton(
                onClick = { onToggleFavoriteClick(pokemonItem) },
                modifier = Modifier
                    .weight(2f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = "",
                    tint = if (pokemonItem.isFavorite) Color(255, 165, 0) else Color(0, 0, 0)
                )
            }
        }
    }
}
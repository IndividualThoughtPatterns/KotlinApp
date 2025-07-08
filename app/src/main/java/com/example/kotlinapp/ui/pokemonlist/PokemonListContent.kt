package com.example.kotlinapp.ui.pokemonlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.data.LoadingState
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.LocalNavController
import com.example.kotlinapp.ui.NavRoutes
import com.example.kotlinapp.ui.PokemonLoadingScreen

@Composable
fun PokemonListContent(
    state: PokemonListScreenState,
    onEvent: (PokemonListEvent) -> Unit,
    modifier: Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val lazyColumnState = rememberLazyListState()
    LaunchedEffect(lazyColumnState) {
        snapshotFlow {
            !lazyColumnState.canScrollForward &&
                    (lazyColumnState.layoutInfo.visibleItemsInfo.isNotEmpty())
        }.collect {
            if (!lazyColumnState.canScrollForward &&
                (lazyColumnState.layoutInfo.visibleItemsInfo.isNotEmpty())
            ) {
                onEvent(PokemonListEvent.OnScrolledBottom)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            state = lazyColumnState
        ) {
            items(items = state.pokemonItemList) { pokemonItem ->
                PokemonElement(
                    modifier = Modifier,
                    pokemonItem = pokemonItem,
                    onToggleFavoriteClick = { pokemonItem: PokemonItem ->
                        onEvent(PokemonListEvent.OnToggleFavoriteClick(pokemonItem))
                    }
                )
            }
        }

        when (state.loadingState) {
            LoadingState.STARTED -> {
                PokemonLoadingScreen(modifier = Modifier)
            }

            LoadingState.SUCCESS -> {}
            LoadingState.FAILED -> {
                LaunchedEffect(state.loadingState) {
                    snackbarHostState.showSnackbar(
                        message = "Ошибка сети",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonElement(
    modifier: Modifier,
    pokemonItem: PokemonItem,
    onToggleFavoriteClick: (pokemonItem: PokemonItem) -> Unit
) {
    val navController = LocalNavController.current
    Card(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(NavRoutes.PokemonInfo(pokemonItem.name))
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)),
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
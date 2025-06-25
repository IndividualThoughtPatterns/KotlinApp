package com.example.kotlinapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlinapp.R
import com.example.kotlinapp.data.PokemonItem
import com.example.kotlinapp.ui.pokemonlist.PokemonListViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pokemonListViewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                pokemonListViewModel.pokemonItemListFlow.collect {
                    setContent {
                        PokemonList(it, pokemonListViewModel)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                pokemonListViewModel.nextPageLoadingStateFlow.collect {
                    it?.let {
                        if (it.isLoaded) {
                            showSuccessMessage()
                        } else {
                            handleNetworkError()
                            Log.d(
                                "next page loading failure",
                                it.error!!.message.toString()
                            )
                        }
                    }
                }
            }
        }

//        recyclerView.addOnScrollListener(
//            object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//
//                    if (!recyclerView.canScrollVertically(1) &&
//                        newState == RecyclerView.SCROLL_STATE_IDLE
//                    ) {
//                        Toast.makeText(context, "загрузка...", Toast.LENGTH_LONG).show()
//
//                        pokemonListViewModel.loadNextPage()
//                    }
//                }
//            }
//        )
    }

    private fun handleNetworkError() {
        Toast.makeText(this, "Ошибка сети", Toast.LENGTH_LONG).show()
    }

    private fun showSuccessMessage() {
        Toast.makeText(this, "Загрузка завершена", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun PokemonList(pokemonItemsList: List<PokemonItem>, pokemonListViewModel: PokemonListViewModel) {
    val state = rememberLazyListState()
    LaunchedEffect(state) {
        snapshotFlow {
            !state.canScrollForward
        }.collect {
            Log.d("mydebug", "srabotalo?")
            if (!state.canScrollForward) {
                pokemonListViewModel.loadNextPage()
                Log.d("mydebug", "doljno srabotat")
            }
        }
    }
    LazyColumn(state = state) {
        items(pokemonItemsList) { pokemonItem ->
            PokemonElement(pokemonItem = pokemonItem)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonElement(pokemonItem: PokemonItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                onClick = {},
                modifier = Modifier
                    .weight(2f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = ""
                )
            }
        }
    }
}








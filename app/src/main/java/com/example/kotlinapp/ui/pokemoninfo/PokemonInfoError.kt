package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun PokemonInfoError(pokemonInfoViewModel: PokemonInfoViewModel) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (pokemonInfoErrorTextRef, pokemonInfoTryAgainButtonRef) = createRefs()

        Text(
            text = "Ошибка сети. Проверьте соединение с интернетом и попробуйте еще раз.",
            modifier = Modifier
                .constrainAs(pokemonInfoErrorTextRef) {
                    top.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                }
        )
        Button(
            onClick = { pokemonInfoViewModel.loadPokemon() },
            modifier = Modifier
                .constrainAs(pokemonInfoTryAgainButtonRef) {
                    absoluteRight.linkTo(pokemonInfoErrorTextRef.absoluteRight)
                    top.linkTo(pokemonInfoErrorTextRef.bottom)
                }
        ) {
            Text(text = "Загрузить")
        }
    }
}
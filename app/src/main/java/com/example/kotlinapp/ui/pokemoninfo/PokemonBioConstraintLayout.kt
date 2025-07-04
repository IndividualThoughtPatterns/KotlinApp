package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun PokemonBioConstraintLayout(modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .then(Modifier.height(IntrinsicSize.Min)) // без этого пиздец будет
    ) {
        val (
            weightConstraintLayoutRef,
            weightHeightDividerRef,
            heightConstraintLayoutRef,
            heightAbilitiesDividerRef,
            abilitiesConstraintLayout,
        ) = createRefs()

        WeightConstraintLayout(
            modifier = Modifier
                .constrainAs(weightConstraintLayoutRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(weightHeightDividerRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .fillMaxHeight()
        )
        VerticalDivider(
            modifier = Modifier.constrainAs(weightHeightDividerRef) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(weightConstraintLayoutRef.absoluteRight)
                absoluteRight.linkTo(heightConstraintLayoutRef.absoluteLeft)
                top.linkTo(parent.top)
            }
        )
        HeightConstraintLayout(
            modifier = Modifier
                .constrainAs(heightConstraintLayoutRef) {
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(weightHeightDividerRef.absoluteRight)
                    absoluteRight.linkTo(heightAbilitiesDividerRef.absoluteLeft)
                    top.linkTo(parent.top)
                }
                .fillMaxHeight()
        )
        VerticalDivider(
            modifier = Modifier.constrainAs(heightAbilitiesDividerRef) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(heightConstraintLayoutRef.absoluteRight)
                absoluteRight.linkTo(abilitiesConstraintLayout.absoluteLeft)
                top.linkTo(parent.top)
            }
        )
        AbilitiesConstraintLayout(
            modifier = Modifier.constrainAs(abilitiesConstraintLayout) {
                bottom.linkTo(parent.bottom)
                absoluteLeft.linkTo(heightAbilitiesDividerRef.absoluteRight)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(parent.top)
            }
        )
    }
}
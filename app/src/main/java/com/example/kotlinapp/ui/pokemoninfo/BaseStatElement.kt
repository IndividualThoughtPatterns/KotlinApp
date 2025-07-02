package com.example.kotlinapp.ui.pokemoninfo

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.kotlinapp.data.BaseStat

@Composable
fun BaseStatElement(baseStat: BaseStat, color: Color) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        val (baseStatsLabelTextRef,
            baseStatsLabelsValuesDividerRef,
            baseStatsValueTextRef,
            hpProgressBarRef
        ) = createRefs()

        createHorizontalChain(
            baseStatsLabelTextRef,
            baseStatsLabelsValuesDividerRef,
            baseStatsValueTextRef,
            hpProgressBarRef,
            chainStyle = ChainStyle.SpreadInside
        )

        Text(
            text = baseStat.baseStatName,
            textAlign = TextAlign.End,
            modifier = Modifier
                .constrainAs(baseStatsLabelTextRef) {
                    centerVerticallyTo(parent)
                }
                .width(40.dp),
            fontWeight = FontWeight.Bold,
            color = color
        )
        VerticalDivider(
            modifier = Modifier
                .constrainAs(baseStatsLabelsValuesDividerRef) {
                    centerVerticallyTo(parent)
                }
        )
        Text(
            text = baseStat.baseStatStringValue,
            modifier = Modifier
                .constrainAs(baseStatsValueTextRef) {
                    centerVerticallyTo(parent)
                }
        )
        LinearProgressIndicator(
            progress = { baseStat.baseStatValue.toFloat() / 233f },
            modifier = Modifier
                .constrainAs(hpProgressBarRef) {
                    centerVerticallyTo(parent)
                }
                .fillMaxWidth(.65f),
            color = color,
            trackColor = color.copy(alpha = 0.24f),
            gapSize = 0.dp,
        )
    }
}
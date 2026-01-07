package com.example.habit.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habit.models.PlantState
import com.example.plant.utils.Randomizer

@Composable
fun PlantItem(
    modifier: Modifier = Modifier,
    randomizer: Randomizer,
    variability: Float,
    plantState: PlantState,
    onAnimate: () -> Unit,
    onNextStage: () -> Unit
) {
    val cellWidth = 200.dp
    val cellHeight = 200.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = plantState.label)

        PlantCanvas(
            randomizer = randomizer,
            variability = variability,
            config = plantState.plantConfig,
            modifier = Modifier
                .width(cellWidth)
                .height(cellHeight)
                .border(1.dp, Color.Gray)
        )
    }
}
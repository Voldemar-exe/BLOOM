package com.example.habit

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plant.utils.Randomizer

@Composable
fun PlantItem(
    modifier: Modifier = Modifier,
    plantState: PlantState,
    onAnimate: () -> Unit = {},
    onNextStage: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = plantState.label, fontSize = 28.sp)

        PlantCanvas(
            modifier =
                Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Gray)
                    .clickable { onItemClick() },
            randomizer = Randomizer(plantState.generationConfig.seed),
            variability = plantState.generationConfig.variability,
            config = plantState.plantConfig,
            onAnimate = onAnimate,
            onNextStage = onNextStage,
        )
    }
}

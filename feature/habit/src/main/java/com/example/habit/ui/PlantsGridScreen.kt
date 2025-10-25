package com.example.habit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit.PlantViewModel
import com.example.habit.utils.DefaultPlantRenderer

@Composable
fun PlantsGridScreen(viewModel: PlantViewModel) {

    LaunchedEffect(Unit) {
        viewModel.initializePlants(6)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(viewModel.plants.value.size) { index ->
            val plant = viewModel.plants.value[index]
            PlantItem(
                plant = plant,
                onAnimate = { viewModel.startAnimation(index) },
                onNextStage = { viewModel.nextGrowthStage(index) },
                renderer = DefaultPlantRenderer()
            )
        }
    }
}
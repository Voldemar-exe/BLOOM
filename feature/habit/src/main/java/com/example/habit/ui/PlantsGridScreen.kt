package com.example.habit.ui

import androidx.compose.runtime.Composable
import com.example.habit.PlantViewModel

@Composable
fun PlantsGridScreen(viewModel: PlantViewModel) {

//    LaunchedEffect(Unit) {
//        viewModel.initializePlants(6)
//    }
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.fillMaxSize(),
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        items(viewModel.plants.value.size) { index ->
//            val plant = viewModel.plants.value[index]
//            PlantItem(
//                plant = plant,
//                onAnimate = { viewModel.startAnimation(index) },
//                onNextStage = { viewModel.nextGrowthStage(index) },
//                renderer = PlantRendererImpl()
//            )
//        }
//    }
}
package com.example.habit.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habit.PlantViewModel
import com.example.habit.models.PlantState

fun parseColor(hex: String): Color {
    val colorLong = hex.removePrefix("#").toLong(16)
    return if (hex.length == 7) Color((colorLong or 0xFF000000L).toInt())
    else Color(colorLong.toInt())
}

// Example usage in a Composable
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DefaultLSystemView() {

    val viewModel = PlantViewModel()

    var shownPlantCard: PlantState? by remember { mutableStateOf(null) }

    Scaffold { paddingValues ->
        SharedTransitionLayout(Modifier.consumeWindowInsets(paddingValues)) {
            AnimatedContent(
                shownPlantCard,
                label = "from grid to card"
            ) { targetState ->
                if (targetState == null) {
                    with(this@SharedTransitionLayout) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(viewModel.plants.value.size) { index ->
                                val plant = viewModel.plants.value[index]
                                PlantItem(
                                    modifier = Modifier
                                        .sharedBounds(
                                            rememberSharedContentState(key = "plant${plant.id}"),
                                            this@AnimatedContent
                                        )
                                        .aspectRatio(0.8f)
                                        .heightIn(max = 200.dp),
                                    plantState = plant,
                                    onAnimate = {},
                                    onNextStage = {},
                                    onItemExpand = { shownPlantCard = plant }
                                )
                            }
                        }
                    }
                } else {
                    with(this@SharedTransitionLayout) {
                        PlantItem(
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(key = "plant${targetState.id}"),
                                    this@AnimatedContent
                                )
                                .aspectRatio(0.8f),
                            plantState = targetState,
                            onAnimate = {},
                            onNextStage = {},
                            onItemExpand = { shownPlantCard = null }
                        )
                    }
                }
            }
        }
    }
}
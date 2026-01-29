package com.example.habit.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.habit.PlantViewModel
import com.example.habit.models.PlantState
import com.example.plant.BranchConfig
import com.example.plant.GenerationConfig
import com.example.plant.LeafConfig
import com.example.plant.RenderConfig
import com.example.plant.utils.LSystemGeneratorImpl
import com.example.plant.utils.Randomizer
import kotlinx.coroutines.delay

// Example usage in a Composable
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DefaultLSystemView() {

    val viewModel = PlantViewModel()

    var shownPlantCard: PlantState? by remember { mutableStateOf(null) }

    val isLeafTesting = false

    Scaffold { paddingValues ->
        if (!isLeafTesting) {
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
                                        onItemClick = { shownPlantCard = plant }
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
                                onItemClick = { shownPlantCard = null }
                            )
                            /*PlantDetailScreen(
                                modifier = Modifier
                                    .sharedBounds(
                                        rememberSharedContentState(key = "plant${targetState.id}"),
                                        this@AnimatedContent
                                    ),
                                plantState = targetState,
                                onBack = { shownPlantCard = null }
                            )*/
                        }
                    }
                }
            }
        } else {
            PaintLeavesByClicks(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Gray),
                config = viewModel.plants.value[0].plantConfig
            )
        }
    }
}
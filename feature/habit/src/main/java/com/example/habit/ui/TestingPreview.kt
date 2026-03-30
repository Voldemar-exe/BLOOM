package com.example.habit.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habit.PlantViewModel
import com.example.habit.models.PlantState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DefaultLSystemView() {
    val viewModel = remember { PlantViewModel() }

    var shownPlantCard: PlantState? by remember { mutableStateOf(null) }

    val plants by viewModel.plants.collectAsStateWithLifecycle()
    val isLeafTesting = false

    Scaffold { paddingValues ->
        if (!isLeafTesting) {
            SharedTransitionLayout(Modifier.consumeWindowInsets(paddingValues)) {
                AnimatedContent(
                    shownPlantCard,
                    label = "from grid to card",
                ) { targetState ->
                    if (targetState == null) {
                        with(this@SharedTransitionLayout) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Button(
                                        onClick = { viewModel.changePreset() },
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Text("Preset")
                                    }

                                    Button(
                                        onClick = { viewModel.changeAllVariability() },
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Text("Variability")
                                    }

                                    Button(
                                        onClick = { viewModel.randomizeLeafTypes() },
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Text("Leaf Type")
                                    }

                                    Button(
                                        onClick = { viewModel.randomizeColors() },
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Text("Color")
                                    }
                                }

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    items(plants.size) { index ->
                                        val plant = plants[index]
                                        PlantItem(
                                            modifier =
                                                Modifier
                                                    .sharedBounds(
                                                        rememberSharedContentState(key = "plant${plant.id}"),
                                                        this@AnimatedContent,
                                                    ).aspectRatio(0.8f)
                                                    .heightIn(max = 200.dp),
                                            plantState = plant,
                                            onAnimate = {},
                                            onNextStage = {},
                                            onItemClick = { shownPlantCard = plant },
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        with(this@SharedTransitionLayout) {
                            PlantItem(
                                modifier =
                                    Modifier
                                        .sharedBounds(
                                            rememberSharedContentState(key = "plant${targetState.id}"),
                                            this@AnimatedContent,
                                        ),
                                //                                    .aspectRatio(0.8f),
                                plantState = targetState,
                                onAnimate = {},
                                onNextStage = {},
                                onItemClick = { shownPlantCard = null },
                            )
                        }
                    }
                }
            }
        } else {
            PaintLeavesByClicks(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Gray),
                config = plants[0].plantConfig,
            )
        }
    }
}

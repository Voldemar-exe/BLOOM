package com.example.habit.embedded.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomIcons
import com.example.habit.PlantCloseUp
import com.example.habit.util.toPlantConfig
import com.example.model.HabitPlant
import com.example.plant.LeafType
import com.example.ui.logic.CollectOneShotEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlantSetupScreen(
    initialPlant: HabitPlant,
    onBack: (HabitPlant?) -> Unit,
    viewModel: PlantSetupViewModel = koinViewModel(),
) {
    LaunchedEffect(initialPlant) {
        viewModel.onAction(PlantSetupAction.Load(initialPlant))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.CollectOneShotEffect { effect ->
        when (effect) {
            is PlantSetupEffect.Saved -> onBack(effect.plant)
        }
    }

    PlantSetupScreen(
        state = state,
        onBack = { onBack(null) },
        onAction = viewModel::onAction,
    )
}

@Composable
fun PlantSetupScreen(
    state: PlantSetupState,
    onBack: () -> Unit,
    onAction: (PlantSetupAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настрой свое растение") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(PlantSetupAction.Undo) }) {
                        Icon(
                            painter = painterResource(BloomIcons.Undo),
                            contentDescription = "undo",
                        )
                    }
                    IconButton(onClick = { onAction(PlantSetupAction.Save) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "undo",
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                PlantCloseUp(
                    seed = state.plant.seed,
                    realProgress = 1f,
                    variability = state.plant.variability,
                    plantConfig = state.plant.toPlantConfig(),
                    extraButton = {
                        FilledIconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(BloomIcons.Random),
                                contentDescription = null,
                            )
                        }
                    },
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
                PlantSelectors(
                    onSelectPetal = { onAction(PlantSetupAction.SelectPetal(it)) },
                    onSelectPlant = { onAction(PlantSetupAction.SelectPlantPreset(it)) },
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Параметры",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            item {
                SliderRow(
                    title = "BranchLength",
                    value = state.plant.baseLength,
                    range = 0f..25f,
                    onChange = { onAction(PlantSetupAction.SetBranchLength(it)) },
                )
                SliderRow(
                    title = "BranchAngle",
                    value = state.plant.baseAngle,
                    range = 0f..90f,
                    onChange = { onAction(PlantSetupAction.SetBranchAngle(it)) },
                )
                SliderRow(
                    title = "WidthReduction",
                    value = state.plant.widthFalloff,
                    range = 0f..1f,
                    onChange = { onAction(PlantSetupAction.SetWidthFalloff(it)) },
                )
                // TODO: Replace with ranged
                SliderRow(
                    title = "BranchWidth",
                    value = state.plant.baseWidth,
                    range = 0f..40f,
                    onChange = { onAction(PlantSetupAction.SetBranchWidth(it)) },
                )
                SliderRow(
                    title = "PetalLength",
                    value = state.plant.petalLength,
                    range = 0f..25f,
                    onChange = { onAction(PlantSetupAction.SetPetalLength(it)) },
                )
            }
        }
    }
}

@Composable
private fun PlantSelectors(
    onSelectPetal: (String) -> Unit,
    onSelectPlant: (Int) -> Unit,
) {
    var showPetal by remember { mutableStateOf(false) }
    var showPlant by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.weight(1f)) {
            Button(onClick = { showPetal = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Выбрать лепесток")
            }
            DropdownMenu(expanded = showPetal, onDismissRequest = { showPetal = false }) {
                LeafType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            onSelectPetal(type.name)
                            showPetal = false
                        },
                    )
                }
            }
        }

        Box(Modifier.weight(1f)) {
            Button(onClick = { showPlant = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Выбрать растение")
            }
            DropdownMenu(expanded = showPlant, onDismissRequest = { showPlant = false }) {
                (0..5).forEach { presetId ->
                    DropdownMenuItem(
                        text = { Text("Preset $presetId") },
                        onClick = {
                            onSelectPlant(presetId)
                            showPlant = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderRow(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = range,
        )
    }
}

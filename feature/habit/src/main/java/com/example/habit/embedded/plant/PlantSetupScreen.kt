package com.example.habit.embedded.plant

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomIcons
import com.example.habit.PlantCloseUp
import com.example.habit.util.toPlantConfig
import com.example.model.HabitPlant
import com.example.plant.LeafType
import com.example.ui.logic.CollectOneShotEffect
import org.koin.compose.viewmodel.koinViewModel

private enum class SetupMode {
    SLIDERS,
    COLORS,
}

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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
        var setupMode by remember { mutableStateOf(SetupMode.SLIDERS) }

        LazyColumn(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                PlantCloseUp(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    seed = state.plant.seed,
                    realProgress = 1f,
                    variability = state.plant.variability,
                    plantConfig = state.plant.toPlantConfig(),
                    extraButton = {
                        FilledIconButton(
                            onClick = {
                                setupMode =
                                    if (setupMode == SetupMode.SLIDERS) {
                                        SetupMode.COLORS
                                    } else {
                                        SetupMode.SLIDERS
                                    }
                            },
                            colors =
                                IconButtonDefaults.iconButtonColors(
                                    containerColor =
                                        if (setupMode ==
                                            SetupMode.SLIDERS
                                        ) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.tertiary
                                        },
                                    contentColor =
                                        if (setupMode ==
                                            SetupMode.SLIDERS
                                        ) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onTertiary
                                        },
                                ),
                        ) {
                            Icon(
                                painter = painterResource(BloomIcons.Palette),
                                contentDescription = null,
                            )
                        }
                    },
                )
            }

            item {
                PlantSelectors(
                    onSelectPetal = { onAction(PlantSetupAction.SelectPetal(it)) },
                    onSelectPlant = { onAction(PlantSetupAction.SelectPlantPreset(it)) },
                )
            }

            item {
                AnimatedContent(
                    targetState = setupMode,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "setup_mode",
                ) { mode ->
                    when (mode) {
                        SetupMode.SLIDERS -> {
                            PlantSliders(
                                plant = state.plant,
                                onAction = onAction,
                            )
                        }

                        SetupMode.COLORS -> {
                            ColorSetupSection(
                                selectedLeafColor = state.plant.petalColor,
                                selectedBaseColor = state.plant.baseColor,
                                onPetalSelect = {
                                    onAction(PlantSetupAction.SetPetalColor(it))
                                },
                                onBranchSelect = {
                                    onAction(PlantSetupAction.SetBaseColor(it))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlantSliders(
    plant: HabitPlant,
    onAction: (PlantSetupAction) -> Unit,
) {
    Column {
        SliderRow(
            title = "Длина ветвей",
            supportiveText = "Минимальный размер ветки",
            value = plant.baseLength,
            range = 0f..25f,
            onChange = { onAction(PlantSetupAction.SetBranchLength(it)) },
        )
        SliderRow(
            title = "Угол ветвей",
            supportiveText = "Угол между стволом и новыми ветками",
            value = plant.baseAngle,
            range = 0f..90f,
            onChange = { onAction(PlantSetupAction.SetBranchAngle(it)) },
        )
        SliderRow(
            title = "Падение ширины",
            supportiveText = "Величина шага при уменьшении ширины веток",
            value = plant.widthFalloff,
            range = 0f..1f,
            onChange = { onAction(PlantSetupAction.SetWidthFalloff(it)) },
        )
        // TODO: Replace with ranged
        SliderRow(
            title = "Ширина ветвей",
            supportiveText = "Максимальная ширина веток",
            value = plant.baseWidth,
            range = 0f..40f,
            onChange = { onAction(PlantSetupAction.SetBranchWidth(it)) },
        )
        SliderRow(
            title = "Длина лепестков",
            supportiveText = "Среднее значение длины лепестков",
            value = plant.petalLength,
            range = 0f..25f,
            onChange = { onAction(PlantSetupAction.SetPetalLength(it)) },
        )
    }
}

private val plantColors =
    listOf(
        0xFF1B5E20,
        0xFF2E7D32,
        0xFF388E3C,
        0xFF43A047,
        0xFF4CAF50,
        0xFF66BB6A,
        0xFF81C784,
        0xFFA5D6A7,
        0xFF2E8B57,
        0xFF6B8E23,
        0xFF9E9D24,
        0xFFAFB42B,
        0xFFC0CA33,
        0xFFD4E157,
        0xFFF9A825,
        0xFFFFB300,
        0xFFFF8F00,
        0xFFFB8C00,
        0xFFF57C00,
        0xFFE65100,
        0xFFE91E63,
        0xFFF06292,
        0xFFEC407A,
        0xFFAB47BC,
        0xFF8E24AA,
        0xFF5C6BC0,
        0xFF42A5F5,
        0xFF3E2723,
        0xFF4E342E,
        0xFF5D4037,
        0xFF6D4C41,
        0xFF795548,
        0xFF8D6E63,
        0xFFA1887F,
        0xFFBCAAA4,
    )

@Composable
private fun ColorSetupSection(
    selectedLeafColor: Long,
    selectedBaseColor: Long,
    onPetalSelect: (Long) -> Unit,
    onBranchSelect: (Long) -> Unit,
) {
    Column {
        Text(
            "Leaf color",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        ColorCarousel(
            colors = plantColors,
            selectedColor = selectedLeafColor,
            onSelect = onPetalSelect,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            "Stem color",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        ColorCarousel(
            colors = plantColors,
            selectedColor = selectedBaseColor,
            onSelect = onBranchSelect,
        )
    }
}

@Composable
private fun ColorCarousel(
    colors: List<Long>,
    selectedColor: Long,
    onSelect: (Long) -> Unit,
    itemSize: Dp = 48.dp,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(colors) { colorLong ->
            val isSelected = selectedColor == colorLong

            Box(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape,
                        ).clickable { onSelect(colorLong) }
                        .height(itemSize)
                        .fillMaxWidth()
                        .padding(2.dp),
                contentAlignment = if (isSelected) Alignment.BottomCenter else Alignment.TopCenter,
            ) {
                Icon(
                    painterResource(BloomIcons.Circle),
                    tint = Color(colorLong),
                    contentDescription = null,
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
    supportiveText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                value.toString(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = range,
        )
        Text(
            supportiveText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

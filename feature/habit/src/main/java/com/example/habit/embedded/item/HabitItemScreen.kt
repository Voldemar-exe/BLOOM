package com.example.habit.embedded.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomIcons
import com.example.habit.PlantCloseUp
import com.example.habit.util.toPlantConfig
import com.example.model.HabitPlant
import com.example.model.Tag
import com.example.ui.components.InputFieldWithClear
import com.example.ui.components.LocalizedDropdownMenu
import com.example.ui.components.RecurrenceSection
import com.example.ui.components.ReminderSection
import com.example.ui.components.TextInputDialog
import com.example.ui.logic.CollectOneShotEffect
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun HabitItemScreen(
    habitId: Long?,
    progress: Float,
    plant: HabitPlant?,
    onBack: () -> Unit,
    onOpenPlantSetup: (HabitPlant) -> Unit,
    viewModel: HabitSetupViewModel = koinViewModel(),
) {
    LaunchedEffect(habitId) {
        habitId?.let { viewModel.onAction(HabitSetupAction.LoadHabit(it, plant)) }
    }
    LaunchedEffect(plant) {
        plant?.let { viewModel.onAction(HabitSetupAction.SetPlant(it)) }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.CollectOneShotEffect { effect ->
        when (effect) {
            HabitSetupEffect.SaveSuccess -> onBack()
        }
    }

    HabitItemScreen(
        state = state,
        progress = progress,
        onAction = viewModel::onAction,
        onBack = onBack,
        onOpenPlantSetup = onOpenPlantSetup,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun HabitItemScreen(
    state: HabitSetupState,
    progress: Float,
    onAction: (HabitSetupAction) -> Unit,
    onBack: () -> Unit,
    onOpenPlantSetup: (HabitPlant) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настрой привычку") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
                actions = {
                    ToggleButton(
                        checked = state.isArchived,
                        onCheckedChange = {
                            onAction(
                                HabitSetupAction.ToggleArchived,
                            )
                        },
                    ) {
                        Icon(
                            painter = painterResource(BloomIcons.Archive),
                            contentDescription = "archive",
                        )
                    }
                    ToggleButton(
                        checked = state.isMuted,
                        onCheckedChange = {
                            onAction(
                                HabitSetupAction.ToggleMuted,
                            )
                        },
                    ) {
                        Icon(
                            painter =
                                painterResource(
                                    if (state.isMuted) {
                                        BloomIcons.AlarmPause
                                    } else {
                                        BloomIcons.AlarmOn
                                    },
                                ),
                            contentDescription = "mute",
                        )
                    }
                    IconButton(onClick = { onAction(HabitSetupAction.OnSaveHabit) }) {
                        Icon(Icons.Default.Check, contentDescription = "save")
                    }
                },
            )
        },
    ) { padding ->

        LazyColumn(modifier = modifier.padding(padding)) {
            item {
                // TODO: It draw differently. I don't know why
                Timber.d("${state.plant}")
                PlantCloseUp(
                    modifier = Modifier.padding(16.dp),
                    seed = state.plant.seed,
                    realProgress = progress,
                    variability = state.plant.variability,
                    plantConfig = state.plant.toPlantConfig(),
                )
            }
            item {
                Button(
                    onClick = { onOpenPlantSetup(state.plant) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                ) {
                    Text("Изменить растение")
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    InputFieldWithClear(
                        label = "Измените название",
                        value = state.title,
                        onValueChange = { onAction(HabitSetupAction.OnTitleChange(it)) },
                    )

                    InputFieldWithClear(
                        label = "Измените описание",
                        value = state.description,
                        onValueChange = { onAction(HabitSetupAction.OnDescriptionChange(it)) },
                    )
                }
            }

            item {
                LocalizedDropdownMenu(
                    icon = BloomIcons.Tag,
                    label =
                        if (state.tags.isEmpty()) {
                            "Выбери несколько тегов"
                        } else {
                            val count = state.tags.size
                            if (count > 1) {
                                state.tags.first().ru + " + ${count - 1}"
                            } else {
                                state.tags.first().ru
                            }
                        },
                    onSelect = { onAction(HabitSetupAction.OnTagClick(it as Tag)) },
                    items = Tag.entries,
                    selectedItems = state.tags.toList(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Интервал и повторение",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            item {
                RecurrenceSection(
                    type = state.recurrence.type,
                    days = state.recurrence.values,
                    onTypeChange = {
                        onAction(
                            HabitSetupAction.SetRecurrenceType(it),
                        )
                    },
                    onDaysChange = { onAction(HabitSetupAction.UpdateSelectedDays(it)) },
                )
            }

            item {
                ReminderSection(
                    reminders = state.reminders,
                    onAdd = { onAction(HabitSetupAction.AddReminder(it)) },
                    onUpdate = { index, time ->
                        onAction(
                            HabitSetupAction.UpdateReminder(
                                index,
                                time,
                            ),
                        )
                    },
                    onToggle = { onAction(HabitSetupAction.ToggleReminder(it)) },
                    onRemove = { onAction(HabitSetupAction.RemoveReminder(it)) },
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Этапы (макс. 3)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            itemsIndexed(state.steps) { index, title ->
                HabitSteps(
                    title = title,
                    checked = index in state.checkedSteps,
                    onToggle = { onAction(HabitSetupAction.ToggleStep(index)) },
                    onRemove = { onAction(HabitSetupAction.RemoveStep(index)) },
                )
            }

            item {
                if (state.steps.size < 3) {
                    var isDialog by remember { mutableStateOf(false) }

                    Button(
                        onClick = { isDialog = true },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add step")
                        Spacer(modifier = Modifier.height(0.dp))
                        Text("Добавить этап привычки")
                    }

                    TextInputDialog(
                        isVisible = isDialog,
                        title = "Новый этап",
                        placeholder = "Введите текст этапа",
                        initialText = "",
                        onDismiss = { isDialog = false },
                        onConfirm = { text ->
                            onAction(HabitSetupAction.AddStep(text))
                            isDialog = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitSteps(
    title: String,
    checked: Boolean,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(12.dp),
                ).clickable { onToggle() }
                .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
        )
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "remove",
                tint = Color.Gray,
            )
        }
    }
}

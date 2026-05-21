package com.example.habit.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.habit.R
import com.example.designsystem.model.AppTheme
import com.example.designsystem.theme.BLOOMTheme
import com.example.designsystem.util.ThemePreviewProvider
import com.example.habit.PlantCanvas
import com.example.habit.util.toHabitPlant
import com.example.habit.util.toPlantConfig
import com.example.model.Habit
import com.example.model.HabitCompletion
import com.example.model.HabitPlant
import com.example.model.HabitWithRelations
import com.example.model.Recurrence
import com.example.model.RecurrenceType
import com.example.plant.utils.PresetLibrary
import com.example.ui.components.DayTimeTabs
import com.example.ui.components.ListTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = koinViewModel(),
    onOpenHabitSetup: (Long?, Float) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HabitScreen(
        state = state,
        onAction = viewModel::onAction,
        onOpenHabitSetup = onOpenHabitSetup,
    )
}

@Composable
fun HabitScreen(
    state: HabitsState,
    onAction: (HabitAction) -> Unit,
    onOpenHabitSetup: (Long?, Float) -> Unit,
) {
    Scaffold(
        topBar = {
            ListTopBar(
                title = stringResource(R.string.habit_screen_title),
                selectedTags = state.selectedFilterTags,
                onTagSelect = { onAction(HabitAction.OnTagSelect(it)) },
                selectedDateRange = state.selectedDate,
                onDateRangeSelect = { date ->
                    onAction(HabitAction.SelectDateRange(date))
                },
                onAddClick = { onOpenHabitSetup(null, 1f) },
                searchQuery = state.searchQuery,
                onSearch = { onAction(HabitAction.Search(it)) },
            )
        },
    ) { padding ->

        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
        ) {
            DayTimeTabs(
                selectedTab = state.selectedTabTime,
                onTabClick = {
                    onAction(HabitAction.SelectTimeInterval(it))
                },
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(state.habits) { habitWithRelations ->

                    val count = state.completionCounts[habitWithRelations.habit.id] ?: 0

                    val progress = (count / 33.0).toFloat().coerceIn(0f, 1f)

                    HabitItem(
                        habit = habitWithRelations.habit,
                        plant = habitWithRelations.plant,
                        progress = progress,
                        onToggle = {
                            onAction(
                                HabitAction.ToggleHabit(habitWithRelations.habit.id),
                            )
                        },
                        onDelete = {
                            onAction(
                                HabitAction.DeleteHabit(habitWithRelations.habit.id),
                            )
                        },
                        onClick = { onOpenHabitSetup(habitWithRelations.habit.id, progress) },
                    )
                }
            }
        }
    }
}

@Composable
fun HabitItem(
    habit: Habit,
    plant: HabitPlant,
    progress: Float,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(end = 16.dp),
                )
            }
        },
        enableDismissFromStartToEnd = false,
        onDismiss = { onDelete() },
    ) {
        OutlinedCard(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
            shape = MaterialTheme.shapes.medium,
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    contentAlignment = Alignment.Center,
                ) {
                    PlantCanvas(
                        modifier = Modifier.fillMaxSize(),
                        progress = progress,
                        seed = plant.seed,
                        variability = plant.variability,
                        config = plant.toPlantConfig(),
                    )
                }

                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val stageText = habit.steps.firstOrNull() ?: habit.description
                    Text(
                        text = stageText,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium,
                    )

                    IconButton(
                        onClick = { onToggle() },
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            imageVector =
                                if (habit.isChecked) {
                                    Icons.Outlined.CheckCircle
                                } else {
                                    Icons.Outlined.AddCircle
                                },
                            contentDescription = "toggle",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HabitPreview(
    @PreviewParameter(ThemePreviewProvider::class)
    appTheme: AppTheme,
) {
    val presets = PresetLibrary.getExamples().take(3)

    BLOOMTheme(appTheme = appTheme) {
        HabitScreen(
            state =
                HabitsState(
                    habits =
                        listOf(
                            HabitWithRelations(
                                habit =
                                    Habit(
                                        id = 1L,
                                        title = "Ежедневная привычка",
                                        description = "Выполняется каждый день",
                                        recurrence = Recurrence(RecurrenceType.DAY, emptySet()),
                                        tags = emptySet(),
                                        steps = listOf("Подготовить место", "Выполнить действие"),
                                        isArchived = false,
                                        isPaused = false,
                                        isMuted = false,
                                        isChecked = true,
                                    ),
                                plant = presets[0].toHabitPlant(),
                                reminders = emptyList(),
                            ),
                            HabitWithRelations(
                                habit =
                                    Habit(
                                        id = 2L,
                                        title = "Привычка на паузе",
                                        description = "Временно приостановлена",
                                        recurrence = Recurrence(RecurrenceType.WEEK, emptySet()),
                                        tags = emptySet(),
                                        steps = emptyList(),
                                        isArchived = false,
                                        isPaused = true,
                                        isMuted = false,
                                        isChecked = false,
                                    ),
                                plant = presets[1].toHabitPlant(),
                                reminders = emptyList(),
                            ),
                            HabitWithRelations(
                                habit =
                                    Habit(
                                        id = 3L,
                                        title = "Архивная привычка",
                                        description = "Больше не используется",
                                        recurrence = Recurrence(RecurrenceType.MONTH, emptySet()),
                                        tags = emptySet(),
                                        steps = emptyList(),
                                        isArchived = true,
                                        isPaused = false,
                                        isMuted = true,
                                        isChecked = false,
                                    ),
                                plant = presets[2].toHabitPlant(),
                                reminders = emptyList(),
                            ),
                        ),
                    completions =
                        List(90) { index ->
                            HabitCompletion(
                                id = index.toLong(),
                                habitId = (index % 4).toLong(),
                                completedAt = 0L,
                                experienceEarned = 0,
                                coinsEarned = 0,
                                createdAt = 0L,
                            )
                        },
                ),
            onAction = {},
            onOpenHabitSetup = { _, _ -> },
        )
    }
}

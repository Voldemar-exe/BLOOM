package com.example.habit.home

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.habit.R
import com.example.habit.PlantCanvas
import com.example.habit.util.toPlantConfig
import com.example.model.Habit
import com.example.model.HabitPlant
import com.example.plant.utils.Randomizer
import com.example.ui.components.DayTimeTabs
import com.example.ui.components.ListTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = koinViewModel(),
    onOpenHabitSetup: (Long?) -> Unit,
) {
    val state by viewModel.habitState.collectAsStateWithLifecycle()

    HabitScreen(
        state = state,
        onAction = viewModel::onAction,
        onOpenHabitSetup = onOpenHabitSetup,
    )
}

@Composable
fun HabitScreen(
    state: HabitState,
    onAction: (HabitAction) -> Unit,
    onOpenHabitSetup: (Long?) -> Unit,
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
                onAddClick = { onOpenHabitSetup(null) },
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
                    HabitItem(
                        habit = habitWithRelations.habit,
                        plant = habitWithRelations.plant,
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
                        onClick = { onOpenHabitSetup(habitWithRelations.habit.id) },
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
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(end = 16.dp),
                )
            }
        },
        enableDismissFromStartToEnd = false,
        onDismiss = { onDelete() },
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable { onClick() },
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                PlantCanvas(
                    modifier =
                        Modifier
                            .height(height = 120.dp)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium,
                            ),
                    randomizer = Randomizer(plant.seed),
                    variability = plant.variability,
                    config = plant.toPlantConfig(),
                    onAnimate = {},
                    onStopAnimate = {},
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                val stageText = habit.steps.firstOrNull() ?: habit.description
                Text(
                    text = stageText,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (habit.isChecked) "Выполнено" else "В процессе",
                        style = MaterialTheme.typography.labelSmall,
                        color =
                            if (habit.isChecked) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                    )

                    IconButton(onClick = { onToggle() }) {
                        Icon(
                            imageVector =
                                if (habit.isChecked) {
                                    Icons.Default.CheckCircle
                                } else {
                                    Icons.Default.AddCircle
                                },
                            contentDescription =
                                if (habit.isChecked) {
                                    "Mark as incomplete"
                                } else {
                                    "Mark as complete"
                                },
                            tint =
                                if (habit.isChecked) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                        )
                    }
                }
            }
        }
    }
}

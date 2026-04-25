package com.example.habit.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                        onToggle = { onAction(HabitAction.ToggleHabit(it)) },
                        onDelete = { onAction(HabitAction.DeleteHabit(it)) },
                        onClick = { onOpenHabitSetup(it) },
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
    onToggle: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onClick: (Long) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clickable { onClick(habit.id) },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            PlantCanvas(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.outline),
                randomizer = Randomizer(plant.seed),
                variability = plant.variability,
                config = plant.toPlantConfig(),
                onAnimate = {},
                onStopAnimate = {},
                onDownload = {},
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = habit.title,
                style = MaterialTheme.typography.titleMedium,
            )

            val stageText =
                habit.steps.firstOrNull()
                    ?: habit.description // TODO: replace with counter

            Text(
                text = stageText,
                style = MaterialTheme.typography.bodySmall,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    checked = habit.isChecked,
                    onCheckedChange = { onToggle(habit.id) },
                )

                IconButton(onClick = { onDelete(habit.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
}

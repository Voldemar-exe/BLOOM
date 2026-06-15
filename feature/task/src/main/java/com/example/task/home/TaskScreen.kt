package com.example.task.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.example.bloom.feature.task.R
import com.example.model.Subtask
import com.example.task.navigation.TaskItemNavKey
import com.example.ui.components.DayTimeTabs
import com.example.ui.components.ListTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = koinViewModel(),
    onNavigate: (NavKey) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TaskScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskScreen(
    state: TaskState,
    onAction: (TaskAction) -> Unit,
    onNavigate: (NavKey) -> Unit,
) {
    Scaffold(
        topBar = {
            ListTopBar(
                title = stringResource(R.string.task_screen_title),
                selectedTags = state.selectedFilterTags,
                onTagSelect = { onAction(TaskAction.OnTagSelect(it)) },
                selectedDateRange = state.selectedDate,
                onDateRangeSelect = { date ->
                    onAction(TaskAction.SelectDateRange(date))
                },
                onAddClick = { onNavigate(TaskItemNavKey(id = null)) },
                searchQuery = state.searchQuery,
                onSearch = { onAction(TaskAction.Search(it)) },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            DayTimeTabs(
                selectedTab = state.selectedTabTime,
                onTabClick = { onAction(TaskAction.SelectTimeInterval(it)) },
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(
                    items = state.tasks.toList(),
                    key = { (task, _) -> task.id },
                ) { (task, subtasks) ->
                    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
                    SwipeToDismissBox(
                        state = swipeToDismissBoxState,
                        backgroundContent = {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    tint = MaterialTheme.colorScheme.error,
                                    contentDescription = "delete",
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        onDismiss = {
                            onAction(TaskAction.DeleteTask(task.id))
                        },
                    ) {
                        TaskItem(
                            title = task.title,
                            description = task.description,
                            isChecked = task.isChecked,
                            subtasks = subtasks,
                            onTaskClick = { onNavigate(TaskItemNavKey(task.id)) },
                            onToggleTask = { onAction(TaskAction.ToggleTask(task.id)) },
                            onToggleSubtask = { onAction(TaskAction.ToggleSubtask(it)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    title: String,
    description: String,
    isChecked: Boolean,
    subtasks: List<Subtask>,
    onTaskClick: () -> Unit,
    onToggleTask: () -> Unit,
    onToggleSubtask: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onTaskClick() },
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    modifier = Modifier.semantics { contentDescription = "checkbox" },
                    checked = isChecked,
                    onCheckedChange = { onToggleTask() },
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    description.takeIf { it.isNotBlank() }?.let { desc ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (subtasks.isNotEmpty()) {
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        colors =
                            IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                    ) {
                        Icon(
                            imageVector =
                                if (isExpanded) {
                                    Icons.Default.KeyboardArrowDown
                                } else {
                                    Icons.Default.KeyboardArrowUp
                                },
                            contentDescription = if (isExpanded) "collapse" else "expand",
                            tint = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                }
            }
        }

        if (isExpanded && subtasks.isNotEmpty()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                subtasks.forEach { subtask ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { onToggleSubtask(subtask.id) }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = subtask.isChecked,
                            onCheckedChange = { onToggleSubtask(subtask.id) },
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = subtask.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

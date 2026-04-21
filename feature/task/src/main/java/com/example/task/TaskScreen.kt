package com.example.task

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.task.R
import com.example.designsystem.picture.BloomIcons
import com.example.ui.DayTimeTabs
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    // TODO: Replace with koin injection
    viewModel: TaskViewModel = koinViewModel(),
) {
    val taskUiState by viewModel.taskUiState.collectAsStateWithLifecycle()

    TaskScreen(
        taskUiState = taskUiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskScreen(
    taskUiState: TaskState,
    onAction: (TaskAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.task_screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(TaskAction.OnFilterClick) }) {
                        Icon(
                            painter = painterResource(BloomIcons.Filter),
                            contentDescription = "Date",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(TaskAction.OnDateClick) }) {
                        Icon(
                            painter = painterResource(BloomIcons.CalendarMonth),
                            contentDescription = "Date",
                        )
                    }
                    IconButton(onClick = { onAction(TaskAction.OnAddClick) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add task",
                        )
                    }
                    IconButton(onClick = { onAction(TaskAction.OnSearchClick) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    }
                },
                expandedHeight = TopAppBarDefaults.MediumAppBarCollapsedHeight,
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
                selectedTab = taskUiState.selectedTabTime,
                onTabClick = { onAction(TaskAction.SelectTimeInterval(it)) },
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(
                    items = taskUiState.tasks.toList(),
                    key = { (task, _) -> task.id },
                ) { (task, subTasks) ->
                    TaskItem(
                        task = task,
                        subTasks = subTasks,
                        onTaskClick = { /* Expand/collapse */ },
                        onToggleTask = { onAction(TaskAction.ToggleTask(task.id)) },
                        onToggleSubTask = { subIndex ->
                            Timber.d("Toggle subtask $subIndex for task ${task.id}")
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: TaskItemState,
    subTasks: List<SubTask>,
    onTaskClick: () -> Unit,
    onToggleTask: () -> Unit,
    onToggleSubTask: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { isExpanded = !isExpanded },
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
                    checked = task.isChecked,
                    onCheckedChange = { onToggleTask() },
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    task.description?.takeIf { it.isNotBlank() }?.let { desc ->
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

                Icon(
                    imageVector =
                        if (isExpanded) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.Default.KeyboardArrowUp
                        },
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (isExpanded && subTasks.isNotEmpty()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                subTasks.forEachIndexed { index, subTask ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { onToggleSubTask(index) }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = subTask.isChecked,
                            onCheckedChange = { onToggleSubTask(index) },
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = subTask.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

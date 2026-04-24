package com.example.task.embedded

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomIcons
import com.example.model.Priority
import com.example.model.Tag
import com.example.ui.components.DatePickDialog
import com.example.ui.components.InputFieldWithClear
import com.example.ui.components.LocalizedDropdownMenu
import com.example.ui.components.RecurrenceSection
import com.example.ui.components.ReminderSection
import com.example.ui.components.TextInputDialog
import com.example.ui.components.convertMillisToDate
import com.example.ui.logic.CollectOneShotEffect
import org.koin.compose.viewmodel.koinViewModel
import java.time.YearMonth

@Composable
fun TaskItemScreen(
    taskId: Long?,
    viewModel: TaskSetupViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    LaunchedEffect(taskId) {
        taskId?.let {
            viewModel.onAction(TaskSetupAction.LoadTask(it))
        }
    }

    val taskItemState by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.CollectOneShotEffect { effect ->
        when (effect) {
            TaskItemEffect.SaveSuccess -> onBack()
        }
    }

    TaskItemScreen(
        taskItemState = taskItemState,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
internal fun TaskItemScreen(
    taskItemState: TaskItemState,
    onAction: (TaskSetupAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настрой свою задачу", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onAction(TaskSetupAction.OnSaveTask)
                        },
                    ) {
                        Icon(Icons.Default.Check, "save")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                InputFieldWithClear(
                    label = "Измените название",
                    value = taskItemState.title,
                    onValueChange = { onAction(TaskSetupAction.OnTitleChange(it)) },
                )

                InputFieldWithClear(
                    label = "Измените описание",
                    value = taskItemState.description,
                    onValueChange = { onAction(TaskSetupAction.OnDescriptionChange(it)) },
                )
            }

            item {
                LocalizedDropdownMenu(
                    icon = BloomIcons.Priority,
                    label = taskItemState.priority.ru,
                    onSelect = {
                        onAction(TaskSetupAction.OnPriorityClick(it as Priority))
                    },
                    items = Priority.entries,
                )
            }

            item {
                LocalizedDropdownMenu(
                    icon = BloomIcons.Tag,
                    label =
                        if (taskItemState.tags.isEmpty()) {
                            "Выбери несколько тегов"
                        } else {
                            val count = taskItemState.tags.size
                            if (count > 1) {
                                taskItemState.tags.first().ru + " + ${taskItemState.tags.size - 1}"
                            } else {
                                taskItemState.tags.first().ru
                            }
                        },
                    onSelect = {
                        onAction(TaskSetupAction.OnTagClick(it as Tag))
                    },
                    items = Tag.entries,
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
                    type = taskItemState.recurrenceType,
                    monthDays = taskItemState.daysOfWeek,
                    currentMonth = YearMonth.now(),
                    onTypeChange = { onAction(TaskSetupAction.SetRecurrenceType(it)) },
                    onDaysChange = { onAction(TaskSetupAction.UpdateSelectedDays(it)) },
                )
            }

            item {
                EndDateToggle(
                    checked = taskItemState.hasEndDate,
                    onToggle = { onAction(TaskSetupAction.ToggleEndDate) },
                )
            }

            item {
                if (taskItemState.hasEndDate) {
                    EndDateField(
                        dateMillis = taskItemState.deadline,
                        onDateSelected = {
                            it?.let {
                                onAction(TaskSetupAction.SetEndDate(it))
                            }
                        },
                    )
                } else {
                    ReminderSection(
                        reminders = taskItemState.reminders,
                        onAdd = { onAction(TaskSetupAction.AddReminder(it)) },
                        onUpdate = { index, time ->
                            onAction(TaskSetupAction.UpdateReminder(index, time))
                        },
                        onToggle = { onAction(TaskSetupAction.ToggleReminder(it)) },
                        onRemove = { onAction(TaskSetupAction.RemoveReminder(it)) },
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Подзадачи (макс. 3)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            itemsIndexed(taskItemState.subtasks) { index, subtask ->
                SubtaskRow(
                    text = subtask.title,
                    isChecked = subtask.isChecked,
                    onRemove = { onAction(TaskSetupAction.RemoveSubtask(index)) },
                    onToggle = { onAction(TaskSetupAction.ToggleSubtask(index)) },
                )
            }

            item {
                if (taskItemState.subtasks.size < 3) {
                    var isInputDialog by remember { mutableStateOf(false) }
                    Button(
                        onClick = { isInputDialog = true },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Добавить подзадачу")
                    }
                    TextInputDialog(
                        isVisible = isInputDialog,
                        title = "Новая подзадача",
                        placeholder = "Введите название",
                        initialText = "",
                        onDismiss = { isInputDialog = false },
                        onConfirm = { title ->
                            onAction(TaskSetupAction.AddSubtask(title))
                            isInputDialog = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SubtaskRow(
    text: String,
    isChecked: Boolean,
    onRemove: () -> Unit,
    onToggle: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(12.dp),
                ).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle() },
        )
        Text(text, modifier = Modifier.weight(1f), fontSize = 14.sp)
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, "remove", tint = Color.Gray)
        }
    }
}

@Composable
fun EndDateField(
    dateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate =
        remember(dateMillis) {
            dateMillis?.let {
                convertMillisToDate(it)
            } ?: "Выберите дату"
        }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            label = { Text("Крайний срок") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                    )
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(64.dp),
        )
    }

    if (showDialog) {
        DatePickDialog(
            onDateSelect = {
                onDateSelected(it)
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
fun EndDateToggle(
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
        )
        Text(
            "Или выбрать конечную дату",
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
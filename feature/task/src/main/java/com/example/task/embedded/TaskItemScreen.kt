package com.example.task.embedded

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomIcons
import com.example.model.RecurrenceType
import com.example.ui.components.TextInputDialog
import com.example.ui.logic.CollectOneShotEffect
import org.koin.compose.viewmodel.koinViewModel

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

                MenuRow(
                    icon = BloomIcons.Priority,
                    text = taskItemState.priority.ru,
                    onClick = { onAction(TaskSetupAction.OnPriorityClick) },
                )

                MenuRow(
                    icon = BloomIcons.Tag,
                    text = "Выбрано тегов: ${taskItemState.tags.size}",
                    onClick = { onAction(TaskSetupAction.OnTagsClick) },
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Интервал и повторение",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            item {
                RecurrenceTabs(
                    selectedType = taskItemState.recurrenceType,
                    onTypeChange = { onAction(TaskSetupAction.SetRecurrenceType(it)) },
                )
            }

            if (taskItemState.recurrenceType == RecurrenceType.WEEK) {
                item {
                    WeekDaysSelector(
                        selectedDays = taskItemState.daysOfWeek,
                        onDayToggle = { onAction(TaskSetupAction.ToggleDay(it)) },
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Напоминание",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )

                LazyColumn(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .heightIn(max = 150.dp),
                ) {
                    if (taskItemState.reminders.isEmpty()) {
                        item { Text(text = "Здесь будут напоминания") }
                    } else {
                        items(taskItemState.reminders) { reminder ->
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            RoundedCornerShape(12.dp),
                                        )
                                        .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    reminder.time.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                                Checkbox(
                                    checked = reminder.isEnabled,
                                    onCheckedChange = { /* TODO */ },
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.clickable { onAction(TaskSetupAction.ToggleEndDate) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = taskItemState.hasEndDate,
                            onCheckedChange = { onAction(TaskSetupAction.ToggleEndDate) },
                        )
                        Text("Или выбрать конечную дату", modifier = Modifier.padding(start = 8.dp))
                    }
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
private fun InputFieldWithClear(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            keyboardOptions =
                KeyboardOptions(imeAction = if (singleLine) ImeAction.Next else ImeAction.Default),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(12.dp),
                    )
                    .padding(end = 8.dp),
        )
    }
}

@Composable
private fun MenuRow(
    icon: Int,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = Color(0xFF6750A4))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
        )
    }
}

@Composable
private fun RecurrenceTabs(
    selectedType: RecurrenceType,
    onTypeChange: (RecurrenceType) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RecurrenceType.entries.forEach { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeChange(type) },
                label = {
                    Text(
                        when (type) {
                            RecurrenceType.DAY -> "День"
                            RecurrenceType.WEEK -> "Неделя"
                            RecurrenceType.MONTH -> "Месяц"
                        },
                    )
                },
                modifier = Modifier.weight(1f),
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD1C4E9),
                        selectedLabelColor = Color(0xFF673AB7),
                    ),
            )
        }
    }
}

@Composable
private fun WeekDaysSelector(
    selectedDays: Set<Int>,
    onDayToggle: (Int) -> Unit,
) {
    val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        days.forEachIndexed { index, label ->
            val isSelected = selectedDays.contains(index)
            Box(
                modifier =
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF6750A4) else Color(0xFFE0E0E0))
                        .clickable { onDayToggle(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                )
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
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
                .padding(12.dp),
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

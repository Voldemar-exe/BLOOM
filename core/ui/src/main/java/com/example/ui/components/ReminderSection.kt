package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.example.model.Reminder

private sealed interface ReminderPickerState {
    data object Add : ReminderPickerState

    data class Edit(val index: Int) : ReminderPickerState
}

@Composable
fun ReminderSection(
    reminders: List<Reminder>,
    onAdd: (Pair<Int, Int>) -> Unit,
    onUpdate: (Int, Pair<Int, Int>) -> Unit,
    onToggle: (Int) -> Unit,
    onRemove: (Int) -> Unit,
) {
    var pickerState by remember { mutableStateOf<ReminderPickerState?>(null) }

    Column {
        Spacer(Modifier.height(16.dp))

        ReminderHeader(
            onAddClick = {
                pickerState = ReminderPickerState.Add
            },
        )

        ReminderList(
            reminders = reminders,
            onClick = { index ->
                pickerState = ReminderPickerState.Edit(index)
            },
            onToggle = onToggle,
            onRemove = onRemove,
        )
    }

    pickerState?.let { state ->
        TimePickDialog(
            onConfirm = { hour, minutes ->
                when (state) {
                    is ReminderPickerState.Add -> onAdd(hour to minutes)
                    is ReminderPickerState.Edit -> onUpdate(state.index, hour to minutes)
                }
                pickerState = null
            },
            onDismiss = { pickerState = null },
        )
    }
}

@Composable
private fun ReminderHeader(onAddClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            "Напоминание",
            style = MaterialTheme.typography.titleMedium,
        )

        IconButton(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = "add")
        }
    }
}

@Composable
private fun ReminderList(
    reminders: List<Reminder>,
    onClick: (Int) -> Unit,
    onToggle: (Int) -> Unit,
    onRemove: (Int) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp),
    ) {
        if (reminders.isEmpty()) {
            Text("Здесь будут напоминания")
        } else {
            reminders.forEachIndexed { index, reminder ->
                ReminderItem(
                    reminder = reminder,
                    onClick = { onClick(index) },
                    onToggle = { onToggle(index) },
                    onRemove = { onRemove(index) },
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ReminderItem(
    reminder: Reminder,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = dismissState,
        onDismiss = { onRemove() },
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(12.dp),
                    ).padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                reminder.time.toString(),
                style = MaterialTheme.typography.bodyLarge,
            )

            Checkbox(
                checked = reminder.isEnabled,
                onCheckedChange = { onToggle() },
            )
        }
    }
}

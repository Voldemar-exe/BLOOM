package com.example.profile.embedded.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.model.AppTheme
import com.example.ui.components.NumberInputDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                SettingsSection(title = "Оформление") {
                    ThemeDropdownMenu(
                        selectedTheme = state.settings.theme,
                        onSelect = { onAction(SettingsAction.ChangeTheme(it)) },
                    )
                }
            }

            item {
                SettingsSection(title = "Цели") {
                    GoalsContent(
                        weeklyGoal = state.settings.weeklyGoal,
                        streakTarget = state.settings.streakTarget,
                        onWeeklyGoalChange = { onAction(SettingsAction.ChangeWeeklyGoal(it)) },
                        onStreakTargetChange = { onAction(SettingsAction.ChangeStreakTarget(it)) },
                    )
                }
            }

            item {
                SettingsSection(title = "Уведомления") {
                    SettingsSwitchItem(
                        title = "Email уведомления",
                        checked = state.settings.emailEnabled,
                        onCheckedChange = { onAction(SettingsAction.ToggleEmail) },
                    )

                    SettingsSwitchItem(
                        title = "Push уведомления",
                        checked = state.settings.pushEnabled,
                        onCheckedChange = { onAction(SettingsAction.TogglePush) },
                    )
                }
            }

            item {
                SettingsSection(title = "Напоминания") {
                    SettingsSwitchItem(
                        title = "Напоминания о привычках",
                        checked = state.settings.habitRemindersEnabled,
                        onCheckedChange = { onAction(SettingsAction.ToggleHabitReminder) },
                    )

                    SettingsSwitchItem(
                        title = "Напоминания о задачах",
                        checked = state.settings.taskRemindersEnabled,
                        onCheckedChange = { onAction(SettingsAction.ToggleTaskReminder) },
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )

        Card {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title)

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun SettingsClickableItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(title)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
        )
    }
}

@Composable
fun ThemeDropdownMenu(
    modifier: Modifier = Modifier,
    selectedTheme: String,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    SettingsClickableItem(
        title = "Тема",
        subtitle = selectedTheme,
        onClick = { expanded = true },
    )

    DropdownMenu(
        modifier = modifier.heightIn(max = 350.dp),
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        AppTheme.entries.forEach { theme ->
            DropdownMenuItem(
                leadingIcon = {
                    if (theme.name == selectedTheme) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "selected",
                        )
                    }
                },
                text = {
                    Text(text = theme.name, fontSize = 16.sp)
                },
                onClick = { onSelect(theme.name) },
            )
        }
    }
}

@Composable
fun GoalsContent(
    weeklyGoal: Int,
    streakTarget: Int,
    onWeeklyGoalChange: (Int) -> Unit,
    onStreakTargetChange: (Int) -> Unit,
) {
    var isWeaklyGoalDialog by remember { mutableStateOf(false) }
    var isStreakTargetDialog by remember { mutableStateOf(false) }

    Column {
        SettingsClickableItem(
            title = "Еженедельно выполнять",
            subtitle = weeklyGoal.toString(),
            onClick = { isWeaklyGoalDialog = true },
        )

        SettingsClickableItem(
            title = "Непрерывно выполнять",
            subtitle = streakTarget.toString(),
            onClick = { isStreakTargetDialog = true },
        )
    }

    NumberInputDialog(
        isVisible = isWeaklyGoalDialog,
        title = "Цель на неделю",
        placeholder = "Количество выполнений",
        initialText = "$weeklyGoal",
        onDismiss = { isWeaklyGoalDialog = false },
        onConfirm = {
            onWeeklyGoalChange(it.toInt())
            isWeaklyGoalDialog = false
        },
    )
    NumberInputDialog(
        isVisible = isStreakTargetDialog,
        title = "Непрерывная цель",
        placeholder = "Количество дней",
        initialText = "$streakTarget",
        onDismiss = { isStreakTargetDialog = false },
        onConfirm = {
            onStreakTargetChange(it.toInt())
            isStreakTargetDialog = false
        },
    )
}

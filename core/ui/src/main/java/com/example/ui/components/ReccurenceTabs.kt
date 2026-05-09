package com.example.ui.components

import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.model.RecurrenceType

@Composable
fun RecurrenceSection(
    type: RecurrenceType,
    days: Set<Int>,
    onTypeChange: (RecurrenceType) -> Unit,
    onDaysChange: (Set<Int>) -> Unit,
) {
    Column {
        RecurrenceTabs(
            selectedType = type,
            onTypeChange = onTypeChange,
        )

        when (type) {
            RecurrenceType.DAY -> {}

            RecurrenceType.WEEK -> {
                WeekDaysSelector(
                    selectedDays = days,
                    onDayToggle = { day ->
                        val newSet =
                            if (day in days) {
                                days - day
                            } else {
                                days + day
                            }
                        onDaysChange(newSet)
                    },
                )
            }

            RecurrenceType.MONTH -> {
                MonthDaysSelector(
                    selectedDays = days,
                    onToggle = { day ->
                        val newSet =
                            if (day in days) days - day else days + day
                        onDaysChange(newSet)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecurrenceTabs(
    selectedType: RecurrenceType,
    onTypeChange: (RecurrenceType) -> Unit,
) {
    LookaheadScope {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    ButtonGroupDefaults.ConnectedSpaceBetween,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RecurrenceType.entries.forEach { type ->
                ToggleButton(
                    checked = selectedType == type,
                    onCheckedChange = { onTypeChange(type) },
                    shapes =
                        when (type) {
                            RecurrenceType.DAY ->
                                ButtonGroupDefaults
                                    .connectedLeadingButtonShapes()

                            RecurrenceType.WEEK ->
                                ButtonGroupDefaults
                                    .connectedMiddleButtonShapes()

                            RecurrenceType.MONTH ->
                                ButtonGroupDefaults
                                    .connectedTrailingButtonShapes()
                        },
                    modifier =
                        Modifier
                            .weight(
                                if (selectedType == type) {
                                    1.5f
                                } else {
                                    1f
                                },
                            ).semantics { role = Role.RadioButton }
                            .animateBounds(this@LookaheadScope),
                ) {
                    Text(
                        when (type) {
                            RecurrenceType.DAY -> "День"
                            RecurrenceType.WEEK -> "Неделя"
                            RecurrenceType.MONTH -> "Месяц"
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MonthDaysSelector(
    selectedDays: Set<Int>,
    onToggle: (Int) -> Unit,
) {
    DaysGrid(
        days = (1..31).map { it.toString() to it },
        selectedIds = selectedDays,
        onToggle = onToggle,
    )
}

@Composable
fun WeekDaysSelector(
    selectedDays: Set<Int>,
    onDayToggle: (Int) -> Unit,
) {
    val weekLabels =
        listOf(
            "Пн" to 0,
            "Вт" to 1,
            "Ср" to 2,
            "Чт" to 3,
            "Пт" to 4,
            "Сб" to 5,
            "Вс" to 6,
        )
    DaysGrid(
        days = weekLabels,
        selectedIds = selectedDays,
        onToggle = onDayToggle,
    )
}

@Composable
private fun DaysGrid(
    days: List<Pair<String, Int>>,
    selectedIds: Set<Int>,
    onToggle: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        days.chunked(7).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowItems.forEach { (label, id) ->
                    val isSelected = id in selectedIds
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(
                                    color =
                                        if (isSelected) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.surfaceDim
                                        },
                                ).clickable { onToggle(id) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = label,
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                        )
                    }
                }
                repeat(7 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

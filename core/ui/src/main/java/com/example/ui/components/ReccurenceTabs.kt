package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            RecurrenceType.DAY -> {
                Text(
                    "Каждый день",
                    modifier = Modifier.padding(16.dp),
                )
            }

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

@Composable
fun MonthDaysSelector(
    selectedDays: Set<Int>,
    onToggle: (Int) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        (0 until 5).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                (1..7).forEach { col ->
                    val day = row * 7 + col

                    if (day <= 31) {
                        val selected = day in selectedDays

                        Box(
                            modifier =
                                Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selected) {
                                            Color(0xFF6750A4)
                                        } else {
                                            Color(0xFFE0E0E0)
                                        },
                                    ).clickable { onToggle(day) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                day.toString(),
                                color = if (selected) Color.White else Color.Black,
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
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

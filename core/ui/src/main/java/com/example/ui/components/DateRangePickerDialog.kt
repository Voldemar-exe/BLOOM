package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.DateRange
import com.example.model.onDateSelected
import java.time.LocalDate
import java.time.YearMonth

private enum class DateCellStatus { None, Start, End, Range, Outside }

@Composable
fun DateRangePickerDialog(
    show: Boolean,
    month: YearMonth,
    selected: DateRange,
    onChange: (DateRange) -> Unit,
    onDismiss: () -> Unit,
) {
    if (!show) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                MonthDateGrid(
                    month = month,
                    selectedDate = selected,
                    onDateSelected = { date ->
                        onChange(selected.onDateSelected(date))
                    },
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    TextButton(onClick = onDismiss) {
                        Text("ОК")
                    }
                }
            }
        }
    }
}

@Composable
fun MonthDateGrid(
    month: YearMonth,
    selectedDate: DateRange,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val gridDays = remember(month) { generateGridDays(month) }

    Column(modifier = modifier) {
        WeekdayHeader(colors)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(gridDays, key = { it?.toEpochDay() ?: 0 }) { date ->
                DateCell(
                    date = date,
                    currentMonth = month,
                    status = date?.toCellStatus(selectedDate) ?: DateCellStatus.Outside,
                    onClick = { if (date?.month == month.month) onDateSelected(date) },
                    colors = colors,
                )
            }
        }
    }
}

@Composable
private fun WeekdayHeader(colors: ColorScheme) {
    val weekdays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        weekdays.forEach { day ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = day,
                    color = colors.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun DateCell(
    date: LocalDate?,
    currentMonth: YearMonth,
    status: DateCellStatus,
    onClick: () -> Unit,
    colors: ColorScheme,
) {
    val isCurrentMonth = date?.month == currentMonth.month
    val text = date?.dayOfMonth?.toString() ?: ""

    val backgroundColor =
        when (status) {
            DateCellStatus.Start, DateCellStatus.End -> colors.primary
            DateCellStatus.Range -> colors.primary.copy(alpha = 0.2f)
            else -> Color.Transparent
        }

    val textColor =
        when (status) {
            DateCellStatus.Start, DateCellStatus.End -> colors.onPrimary
            else -> colors.onSurface.copy(alpha = if (isCurrentMonth) 1f else 0.38f)
        }

    Box(
        modifier =
            Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(
                    when (status) {
                        DateCellStatus.Range -> backgroundColor
                        else -> Color.Transparent
                    },
                )
                .clickable(enabled = isCurrentMonth) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (status == DateCellStatus.Start || status == DateCellStatus.End) {
            Box(
                modifier =
                    Modifier
                        .size(36.dp)
                        .background(backgroundColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text, color = textColor)
            }
        } else {
            Text(text, color = textColor)
        }
    }
}

private fun generateGridDays(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val offset = (firstDay.dayOfWeek.value - 1) % 7
    val days = mutableListOf<LocalDate?>()
    for (i in offset downTo 1) days.add(firstDay.minusDays(i.toLong()))
    for (d in 1..month.lengthOfMonth()) days.add(firstDay.plusDays((d - 1).toLong()))
    val remaining = 42 - days.size
    for (i in 1..remaining) days.add(days.last()!!.plusDays(i.toLong()))
    return days
}

private fun LocalDate.toCellStatus(selected: DateRange): DateCellStatus {
    val start = selected.start ?: return DateCellStatus.None
    val end = selected.end ?: start

    return when {
        this == start && this == end -> DateCellStatus.Start
        this == start -> DateCellStatus.Start
        this == end -> DateCellStatus.End
        this.isAfter(start) && this.isBefore(end) -> DateCellStatus.Range
        else -> DateCellStatus.None
    }
}

package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.DayTimeInterval

@Composable
private fun DayTimeTabs(
    selectedTab: DayTimeInterval,
    onTabClick: (DayTimeInterval) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DayTimeInterval.entries.forEach { timeInterval ->
            FilterChip(
                selected = selectedTab == timeInterval,
                onClick = { onTabClick(timeInterval) },
                label = {
                    Text(
                        when (timeInterval) {
                            DayTimeInterval.TODAY -> "Сегодня"
                            DayTimeInterval.TOMORROW -> "Завтра"
                            DayTimeInterval.ALL -> "Все"
                        },
                    )
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

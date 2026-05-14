package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.designsystem.picture.BloomIcons
import com.example.model.DateRange
import com.example.model.Tag
import java.time.YearMonth

@Composable
fun ListTopBar(
    title: String,
    selectedTags: Set<Tag>,
    onTagSelect: (Tag) -> Unit,
    selectedDateRange: DateRange,
    onDateRangeSelect: (DateRange) -> Unit,
    onAddClick: () -> Unit,
    searchQuery: String,
    onSearch: (String) -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            TagDropdownMenu(
                icon = BloomIcons.Filter,
                selectedTags = selectedTags,
                onSelect = onTagSelect,
            )
        },
        actions = {
            var isDatePickVisible by remember { mutableStateOf(false) }

            IconButton(onClick = { isDatePickVisible = true }) {
                Icon(
                    painter =
                        painterResource(
                            if (selectedDateRange.start == null) {
                                BloomIcons.CalendarMonth
                            } else {
                                BloomIcons.CalendarCheck
                            },
                        ),
                    contentDescription = "date",
                )
            }
            DateRangePickerDialog(
                show = isDatePickVisible,
                month = YearMonth.now(),
                selected = selectedDateRange,
                onChange = onDateRangeSelect,
                onDismiss = { isDatePickVisible = false },
                onClear = { onDateRangeSelect(DateRange(null, null)) },
            )

            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add",
                )
            }
            var isSearchVisible by remember { mutableStateOf(false) }
            val canClearSearch = isSearchVisible && searchQuery.isNotEmpty()

            IconButton(
                onClick = {
                    if (canClearSearch) {
                        onSearch("")
                    } else {
                        isSearchVisible = true
                    }
                },
            ) {
                Icon(
                    imageVector =
                        if (canClearSearch) {
                            Icons.Default.Clear
                        } else {
                            Icons.Default.Search
                        },
                    contentDescription = "search",
                )
            }
            TextInputDialog(
                isVisible = isSearchVisible,
                title = "Поиск",
                placeholder = "Текст названия или описания",
                onDismiss = { isSearchVisible = false },
                onConfirm = {
                    onSearch(it)
                    isSearchVisible = false
                },
            )
        },
        expandedHeight = TopAppBarDefaults.MediumAppBarCollapsedHeight,
    )
}

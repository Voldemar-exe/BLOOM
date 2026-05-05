package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.designsystem.picture.BloomIcons
import com.example.model.SortType

@Composable
fun FilteredDropdownMenu(
    selectedSortType: SortType,
    onSortChange: (SortType) -> Unit,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { sortMenuExpanded = true }) {
            Icon(
                painter = painterResource(BloomIcons.Filter),
                contentDescription = "Сортировка",
            )
        }
        DropdownMenu(
            expanded = sortMenuExpanded,
            onDismissRequest = { sortMenuExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("По ID (возрастание)") },
                onClick = {
                    onSortChange(SortType.ID_ASC)
                    sortMenuExpanded = false
                },
                trailingIcon = {
                    if (selectedSortType == SortType.ID_ASC) {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                },
            )
            DropdownMenuItem(
                text = { Text("По прогрессу (убывание)") },
                onClick = {
                    onSortChange(SortType.DESC)
                    sortMenuExpanded = false
                },
                trailingIcon = {
                    if (selectedSortType == SortType.DESC) {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                },
            )
            DropdownMenuItem(
                text = { Text("По прогрессу (возрастание)") },
                onClick = {
                    onSortChange(SortType.ASC)
                    sortMenuExpanded = false
                },
                trailingIcon = {
                    if (selectedSortType == SortType.ASC) {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                },
            )
        }
    }
}

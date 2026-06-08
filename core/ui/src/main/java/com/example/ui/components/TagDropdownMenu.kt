package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Tag

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TagDropdownMenu(
    modifier: Modifier = Modifier,
    icon: Int,
    selectedTags: Set<Tag>,
    onSelect: (Tag) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val sortedTags =
        remember(selectedTags) {
            Tag.entries.sortedWith(compareByDescending { it in selectedTags })
        }
    Box {
        IconButton(
            onClick = { expanded = !expanded },
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "tag-control",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DropdownMenu(
            modifier =
                modifier
                    .heightIn(max = 350.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = ShapeDefaults.Medium,
                    ),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            sortedTags.forEach { tag ->
                DropdownMenuItem(
                    leadingIcon = {
                        if (tag in selectedTags) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "selected",
                            )
                        }
                    },
                    shape = ShapeDefaults.Medium,
                    text = {
                        Text(text = tag.ru, fontSize = 16.sp)
                    },
                    onClick = { onSelect(tag) },
                )
            }
        }
    }
}

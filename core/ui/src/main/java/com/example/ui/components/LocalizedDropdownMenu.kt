package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LocalizedEnum

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LocalizedDropdownMenu(
    modifier: Modifier = Modifier,
    icon: Int,
    label: String,
    onSelect: (LocalizedEnum) -> Unit,
    selectedItems: List<LocalizedEnum>,
    items: List<LocalizedEnum>,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec =
            tween(
                durationMillis = 300,
            ),
        label = "",
    )

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Icon(
                    painter = painterResource(icon),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                )
                Spacer(Modifier.padding(horizontal = 4.dp))
                Text(text = label)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .padding(start = 8.dp)
                            .rotate(rotationState),
                )
            }
        }

        ExposedDropdownMenu(
            modifier =
                Modifier
                    .exposedDropdownSize(true)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = ShapeDefaults.Medium,
                    ),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = {
                        if (item in selectedItems) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "selected",
                            )
                        }
                    },
                    shape = ShapeDefaults.Medium,
                    text = {
                        Text(text = item.ru, fontSize = 16.sp)
                    },
                    onClick = { onSelect(item) },
                )
            }
        }
    }
}

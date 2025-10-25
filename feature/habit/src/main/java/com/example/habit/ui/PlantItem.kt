package com.example.habit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import com.example.habit.models.PlantState
import com.example.habit.utils.IPlantRenderer

@Composable
fun PlantItem(
    plant: PlantState,
    onAnimate: () -> Unit,
    onNextStage: () -> Unit,
    renderer: IPlantRenderer
) {
    val cellWidth = 200.dp
    val cellHeight = 200.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = plant.label)

        Canvas(
            modifier = Modifier
                .width(cellWidth)
                .height(cellHeight)
                .border(1.dp, Color.Gray)
        ) {
            val x = size.width / 2f
            val y = size.height
            clipRect {
                renderer.drawPlant(
                    drawScope = this,
                    commands = plant.commands,
                    offset = Offset(x, y),
                    progress = plant.progress,
                    cellSize = size
                )
            }
        }

        Row {
            IconButton(onClick = onAnimate) {
                Icon(Icons.Default.Refresh, null)
            }
            IconButton(onClick = onNextStage) {
                Icon(Icons.Default.KeyboardArrowUp, null)
            }
        }
    }
}
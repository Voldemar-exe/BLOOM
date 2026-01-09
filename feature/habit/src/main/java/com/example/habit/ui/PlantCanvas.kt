package com.example.habit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import com.example.plant.PlantConfig
import com.example.plant.utils.PlantData
import com.example.plant.utils.PlantRendererImpl
import com.example.plant.utils.Randomizer

@Composable
fun PlantCanvas(
    randomizer: Randomizer,
    variability: Float,
    config: PlantConfig,
    modifier: Modifier = Modifier
) {
    val plantRenderer = remember { PlantRendererImpl() }
    var plantData by remember(config.lSystemSentence, variability) {
        mutableStateOf<PlantData?>(null)
    }

    Canvas(modifier) {
        val canvasCenterX = size.width / 2f
        val canvaBottomY = size.height

        if (plantData == null) {
            val data = plantRenderer.generatePlantData(
                offset = Offset(canvasCenterX, canvaBottomY),
                progress = 1f,
                variability = variability,
                lSystemSentence = config.lSystemSentence,
                plantConfig = config,
                randomizer = randomizer
            )
            plantData = data
        }

        plantData?.let { data ->

            val padding = 10f

            val plantBounds = data.bounds
            val plantWidth = plantBounds.width
            val plantHeight = plantBounds.height

            val canvasWidth = size.width - padding
            val canvasHeight = size.height - padding

            val scaleFactor = minOf(canvasWidth / plantWidth, canvasHeight / plantHeight)


            withTransform({
                scale(
                    scaleX = scaleFactor,
                    scaleY = scaleFactor,
                    pivot = Offset(canvasCenterX, canvaBottomY)
                )
                translate(left = canvasCenterX - (data.bounds.center.x))
            }) {
                plantRenderer.drawPlantData(
                    drawScope = this,
                    plantData = data,
                    plantConfig = config
                )

                // FIXME: Remove after debug
                drawRect(
                    color = Color.Green.copy(alpha = 0.3f),
                    topLeft = data.bounds.topLeft,
                    size = data.bounds.size
                )
            }
        }
    }
}
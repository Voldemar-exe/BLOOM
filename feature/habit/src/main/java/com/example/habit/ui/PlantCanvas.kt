package com.example.habit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import com.example.plant.PlantConfig
import com.example.plant.utils.PlantRendererImpl
import com.example.plant.utils.Randomizer

@Composable
fun PlantCanvas(
    randomizer: Randomizer,
    variability: Float,
    config: PlantConfig,
    modifier: Modifier = Modifier
) {
    Canvas(modifier) {
//        drawRect(
//            brush = Brush.linearGradient(
//                0.0f to config.backgroundColorTop,
//                1.0f to config.backgroundColorBottom,
//                start = Offset(0f, 0f),
//                end = Offset(0f, size.height * 0.7f)
//            ),
//            size = size
//        )
        val canvasCenterX = size.width / 2f
        val canvasBottomY = size.height
        val worldWidth = 800f
        val scaleFactor = size.width / worldWidth

        PlantRendererImpl().drawPlant(
            drawScope = this,
            offset = Offset(canvasCenterX, canvasBottomY),
            progress = 1f,
            cellSize = Size(worldWidth, size.height / scaleFactor),
            variability = variability,
            lSystemSentence = config.lSystemSentence,
            plantConfig = config,
            randomizer = randomizer
        )

        // TODO: Realize render with transforming only canvas
        withTransform({
            translate(left = canvasCenterX, top = canvasBottomY)
            scale(scaleX = scaleFactor, scaleY = scaleFactor)
        }) {
            drawLine(
                color = Color.Magenta,
                start = Offset(0f, 100f),
                end = Offset(10f, 500f),
                strokeWidth = 4f
            )
            PlantRendererImpl().drawPlant(
                drawScope = this,
                offset = Offset(0f, 0f),
                progress = 1f,
                cellSize = Size(worldWidth, size.height / scaleFactor),
                variability = variability,
                lSystemSentence = config.lSystemSentence,
                plantConfig = config,
                randomizer = randomizer
            )
        }

        // : Draw dot in Bottom Center for testing
        drawCircle(
            color = Color.Red,
            radius = 8f,
            center = Offset(canvasCenterX, canvasBottomY)
        )
    }
}
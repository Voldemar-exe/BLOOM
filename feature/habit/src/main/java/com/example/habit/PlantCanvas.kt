package com.example.habit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import com.example.plant.PlantConfig
import com.example.plant.utils.LSystemInterpreterImpl
import com.example.plant.utils.PathBuilderImpl
import com.example.plant.utils.PlantGeometry
import com.example.plant.utils.PlantRendererImpl
import com.example.plant.utils.Randomizer

@Composable
fun PlantCanvas(
    modifier: Modifier = Modifier,
    innerCanvasPadding: Float = 10f,
    randomizer: Randomizer,
    variability: Float,
    config: PlantConfig,
    onAnimate: () -> Unit,
    onStopAnimate: () -> Unit,
) {
    val lSystemInterpreter = remember { LSystemInterpreterImpl() }
    val pathBuilder = remember { PathBuilderImpl() }
    val plantRenderer = remember { PlantRendererImpl() }
    var canvasSize by remember { mutableStateOf<Size?>(null) }

    val plantGeometry by remember(config, variability, randomizer, canvasSize) {
        derivedStateOf {
            canvasSize?.let { size ->
                lSystemInterpreter.generatePoints(
                    offset = Offset(size.width / 2f, size.height),
                    variability = variability,
                    lSystemSentence = config.lSystemSentence,
                    branchConfig = config.branchConfig,
                    leafConfig = config.leafConfig,
                    randomizer = randomizer,
                )
            }
        }
    }

    val plantPaths by remember(plantGeometry) {
        derivedStateOf { plantGeometry?.let { pathBuilder.buildPlant(it) } }
    }

    val transformParams =
        remember(plantGeometry, canvasSize, innerCanvasPadding) {
            canvasSize?.let { size ->
                calculateTransformParams(plantGeometry!!, size, innerCanvasPadding)
            }
        }

    Canvas(
        modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { intSize ->
                val newSize = Size(intSize.width.toFloat(), intSize.height.toFloat())
                if (canvasSize != newSize) {
                    canvasSize = newSize
                }
            },
    ) {
        plantPaths?.let { paths ->
            transformParams?.let { params ->
                withTransform({
                    scale(
                        scaleX = params.scaleFactor,
                        scaleY = params.scaleFactor,
                        pivot = params.pivot,
                    )
                    translate(left = params.translateX)
                }) {
                    plantRenderer.drawPlant(
                        drawScope = this,
                        plantPaths = paths,
                        renderConfig = config.renderConfig,
                    )
                }
            }
        }
    }
}

private data class TransformParams(
    val scaleFactor: Float,
    val pivot: Offset,
    val translateX: Float,
)

private fun calculateTransformParams(
    data: PlantGeometry,
    size: Size,
    innerCanvasPadding: Float,
): TransformParams {
    val canvasCenterX = size.width / 2f
    val canvasBottomY = size.height
    val plantBounds = data.bounds
    val plantWidth = plantBounds.width
    val plantHeight = plantBounds.height + 100f
    val canvasWidth = size.width - innerCanvasPadding
    val canvasHeight = size.height - innerCanvasPadding
    val scaleFactor = minOf(canvasWidth / plantWidth, canvasHeight / plantHeight)

    return TransformParams(
        scaleFactor = scaleFactor,
        pivot = Offset(canvasCenterX, canvasBottomY),
        translateX = canvasCenterX - data.bounds.center.x,
    )
}

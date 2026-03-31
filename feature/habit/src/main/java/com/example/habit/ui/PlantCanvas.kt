package com.example.habit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import com.example.plant.utils.LSystemInterpreterImpl
import com.example.plant.utils.LeafPrimitive
import com.example.plant.utils.PathBuilderImpl
import com.example.plant.utils.PlantGeometry
import com.example.plant.utils.PlantRendererImpl
import com.example.plant.utils.Randomizer
import kotlin.math.atan2
import kotlin.math.sqrt

@Composable
fun PlantCanvas(
    modifier: Modifier = Modifier,
    innerCanvasPadding: Float = 10f,
    randomizer: Randomizer,
    variability: Float,
    config: PlantConfig,
    onAnimate: () -> Unit,
    onNextStage: () -> Unit,
) {
    val lSystemInterpreter = remember { LSystemInterpreterImpl() }
    val pathBuilder = remember { PathBuilderImpl() }
    val plantRenderer = remember { PlantRendererImpl() }
    var canvasSize by remember { mutableStateOf<Size?>(null) }

    val plantGeometry by remember(config, variability, randomizer) {
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
        derivedStateOf {
            plantGeometry?.let { geometry ->
                pathBuilder.buildPlant(geometry)
            }
        }
    }

    val transformParams =
        remember(plantGeometry, canvasSize, innerCanvasPadding) {
            canvasSize?.let { size ->
                calculateTransformParams(plantGeometry!!, size, innerCanvasPadding)
            }
        }

    Canvas(modifier.clipToBounds()) {
        canvasSize = size

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

@Composable
fun PaintLeavesByClicks(
    modifier: Modifier = Modifier,
    config: PlantConfig,
) {
    val plantRenderer = remember { PlantRendererImpl() }

    var startPoint by remember { mutableStateOf<Offset?>(null) }
    val drawnElements = remember { mutableStateListOf<Pair<Offset, Offset>>() }

    Canvas(
        modifier =
            modifier
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val firstPoint = Offset(down.position.x, down.position.y)

                        if (startPoint == null) {
                            startPoint = firstPoint
                        } else {
                            val first = startPoint!!
                            val secondPoint = firstPoint
                            drawnElements.add(first to secondPoint)
                            startPoint = null
                        }
                    }
                },
    ) {
        val canvasCenterX = size.width / 2f
        val canvasBottomY = size.height

        drawnElements.forEach { points ->
            val (start, end) = points
            val dx = end.x - start.x
            val dy = end.y - start.y
            val angle = -atan2(dy, dx)
            val length = sqrt(dx * dx + dy * dy)

            val leaf =
                LeafPrimitive(
                    position = start,
                    angle = angle.toDouble(),
                    length = length,
                    type = LeafType.TYPE1,
                )

            PlantRendererImpl().drawLeaf(
                this,
                PathBuilderImpl().buildLeafPath(leaf),
                config.renderConfig.leafColor.copy(alpha = config.renderConfig.leafAlpha),
            )
        }

        startPoint?.let { pt ->
            drawCircle(color = Color.Red, radius = 8f, center = pt)
        }
    }
}

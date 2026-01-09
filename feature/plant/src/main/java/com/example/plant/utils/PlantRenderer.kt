package com.example.plant.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import com.example.plant.PlantTheme
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

private class UnknownLSystemCommand(override val message: String?) : Exception()

data class BranchPrimitive(
    val startPoint: Offset,
    val endPoint: Offset,
    val startWidth: Float,
    val endWidth: Float,
    val angle: Float
)

data class LeafPrimitive(
    val position: Offset,
    val angle: Double,
    val width: Float,
    val length: Float,
    val type: LeafType
)

data class PlantData(
    val branches: List<BranchPrimitive>,
    val leaves: List<LeafPrimitive>,
    val bounds: Rect
)

interface PlantRenderer {

    fun generatePlantData(
        offset: Offset = Offset(0f, 0f),
        progress: Float,
        variability: Float,
        lSystemSentence: String,
        plantConfig: PlantConfig,
        randomizer: Randomizer
    ): PlantData

    fun drawPlantData(
        drawScope: DrawScope,
        plantData: PlantData,
        plantConfig: PlantConfig
    )
}

class PlantRendererImpl : PlantRenderer {

    override fun generatePlantData(
        offset: Offset,
        progress: Float,
        variability: Float,
        lSystemSentence: String,
        plantConfig: PlantConfig,
        randomizer: Randomizer
    ): PlantData {
        val branches = mutableListOf<BranchPrimitive>()
        val leaves = mutableListOf<LeafPrimitive>()

        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        var pos = offset
        var angle = 90.0
        var width = plantConfig.branches.width
        val falloff = 1 - plantConfig.branches.widthFalloff
        val stack = mutableListOf<Triple<Offset, Double, Float>>()

        for (c in lSystemSentence) {
            when (c) {
                'F' -> {
                    val startWidth = width
                    val endWidth = max(
                        startWidth * falloff.pow(0.1f),
                        plantConfig.branches.minWidth
                    )
                    width = endWidth
                    val lengthChange = randomizer.nextFloatAround(1f, variability)
                    val length = plantConfig.branches.length * lengthChange
                    val rad = toRadians(angle).toFloat()
                    val dx = length * cos(rad)
                    val dy = -length * sin(rad)
                    val newPos = pos + Offset(dx, dy)

                    minX = min(minX, newPos.x)
                    minY = min(minY, newPos.y)
                    maxX = max(maxX, newPos.x)
                    maxY = max(maxY, newPos.y)

                    branches.add(BranchPrimitive(
                        startPoint = pos,
                        endPoint = newPos,
                        startWidth = startWidth,
                        endWidth = endWidth,
                        angle = rad
                    ))

                    pos = newPos
                }
                '+' -> {
                    val rotChange = randomizer.nextFloatAround(1f, variability)
                    angle += plantConfig.branches.angle.toDouble() * rotChange
                }
                '-' -> {
                    val rotChange = randomizer.nextFloatAround(1f, variability)
                    angle -= plantConfig.branches.angle.toDouble() * rotChange
                }
                '[' -> {
                    stack.add(Triple(pos, angle, width))
                }
                ']' -> {
                    val leafWidth = randomizer.nextFloatAround(plantConfig.leaves.width, variability)
                    val leafLength = randomizer.nextFloatAround(plantConfig.leaves.length, variability)

                    leaves.add(LeafPrimitive(
                        position = pos,
                        angle = angle,
                        width = leafWidth,
                        length = leafLength,
                        type = plantConfig.leaves.type
                    ))

                    if (stack.isNotEmpty()) {
                        val (p, a, w) = stack.removeAt(stack.size - 1)
                        pos = p
                        angle = a
                        width = w
                    }
                }
                'X' -> {}
                else -> {
                    throw UnknownLSystemCommand("Command $c")
                }
            }
        }

        val bounds = Rect(
            minX - (plantConfig.leaves.length + variability),
            minY - (plantConfig.leaves.length + variability),
            maxX + (plantConfig.leaves.length + variability),
            maxY
        )

        return PlantData(branches, leaves, bounds)
    }

    override fun drawPlantData(
        drawScope: DrawScope,
        plantData: PlantData,
        plantConfig: PlantConfig
    ) {
        for (branch in plantData.branches) {
            drawBranch(drawScope, branch, plantConfig.theme.branchColor)
        }

        for (leaf in plantData.leaves) {
            drawLeafFromPrimitive(drawScope, leaf, plantConfig.theme)
        }
    }

    private fun drawBranch(
        drawScope: DrawScope,
        branch: BranchPrimitive,
        color: Color
    ) {
        val perp = Offset(
            sin(branch.angle),
            cos(branch.angle)
        )

        val startLeft = branch.startPoint + perp * (branch.startWidth / 2)
        val startRight = branch.startPoint - perp * (branch.startWidth / 2)
        val endLeft = branch.endPoint + perp * (branch.endWidth / 2)
        val endRight = branch.endPoint - perp * (branch.endWidth / 2)

        drawScope.drawPath(
            path = Path().apply {
                moveTo(startLeft.x, startLeft.y)
                lineTo(startRight.x, startRight.y)
                lineTo(endRight.x, endRight.y)
                lineTo(endLeft.x, endLeft.y)
                close()
            },
            color = color
        )
    }

    private fun drawLeafFromPrimitive(
        drawScope: DrawScope,
        leaf: LeafPrimitive,
        plantTheme: PlantTheme
    ) {
        val rad = toRadians(leaf.angle).toFloat()
        val cosr = cos(rad)
        val sinr = sin(rad)
        val leafColor = plantTheme.leafColor.copy(alpha = plantTheme.leafAlpha)

        fun rotateAndTranslate(ptX: Float, ptY: Float): Offset {
            val rx = ptX * cosr - ptY * sinr
            val ry = ptX * sinr + ptY * cosr
            return leaf.position + Offset(rx, ry)
        }

        val path = Path()
        when (leaf.type) {
            LeafType.TYPE0 -> {
                val points = listOf(
                    0f to 0f, 1f to -1f, 0f to -4f, -1f to -1f, 0f to 0f
                )
                points.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * leaf.width, y * leaf.length)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                drawScope.drawPath(path, leafColor)
            }

            LeafType.TYPE1 -> {
                (0..360 step 10).forEach { i ->
                    val theta = toRadians(i.toDouble()).toFloat()
                    val px = 2 * leaf.width * cos(theta)
                    val py = -2 * leaf.length + 2 * leaf.length * sin(theta)
                    val abs = rotateAndTranslate(px, py)
                    if (i == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                path.close()
                drawScope.drawPath(path, leafColor)
            }

            LeafType.TYPE2 -> {

                val points = listOf(
                    0f to 0f, 1f to -1f, 1f to -4f, 0f to -5f, -1f to -4f, -1f to -1f, 0f to 0f
                )
                points.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * leaf.width, y * leaf.length)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                drawScope.drawPath(path, leafColor)

                path.reset()
                val rectPoints = listOf(
                    0f to 0f, 0.25f to 0f, 0.25f to -5f, 0f to -5f, 0f to 0f
                )
                rectPoints.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * leaf.width, y * leaf.length)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                drawScope.drawPath(path, leafColor)
            }
        }
    }

}
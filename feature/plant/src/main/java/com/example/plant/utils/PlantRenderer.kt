package com.example.plant.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

interface PlantRenderer {
    fun drawPlant(
        drawScope: DrawScope,
        offset: Offset,
        progress: Float,
        cellSize: Size,
        variability: Float,
        lSystemSentence: String,
        plantConfig: PlantConfig,
        randomizer: Randomizer
    ): DrawScope
}

class PlantRendererImpl : PlantRenderer {
    override fun drawPlant(
        drawScope: DrawScope,
        offset: Offset,
        progress: Float,
        cellSize: Size,
        variability: Float,
        lSystemSentence: String,
        plantConfig: PlantConfig,
        randomizer: Randomizer
    ) = drawScope.apply {

        var pos = offset
        var angle = 90.0
        var width = plantConfig.branches.width
        val falloff = 1 - plantConfig.branches.widthFalloff
        val stack = mutableListOf<Triple<Offset, Double, Float>>()

        for (c in lSystemSentence) {
            if (c == 'F') {
                val startWidth = width
                val endWidth = max(startWidth * falloff.pow(0.1f), plantConfig.branches.minWidth)
                width = endWidth
                val lengthChange = randomizer.nextFloatAround(1f, variability)
                val length = plantConfig.branches.length * lengthChange
                val rad = toRadians(angle).toFloat()
                val dx = length * cos(rad)
                val dy = -length * sin(rad)
                val newPos = pos + Offset(dx, dy)

                val ux = cos(rad)
                val uy = -sin(rad)
                val px = -uy
                val py = ux
                val perp = Offset(px, py)

                val startLeft = pos + perp * (startWidth / 2)
                val startRight = pos - perp * (startWidth / 2)
                val endLeft = newPos + perp * (endWidth / 2)
                val endRight = newPos - perp * (endWidth / 2)

                drawPath(
                    path = Path().apply {
                        moveTo(startLeft.x, startLeft.y)
                        lineTo(startRight.x, startRight.y)
                        lineTo(endRight.x, endRight.y)
                        lineTo(endLeft.x, endLeft.y)
                        close()
                    },
                    color = plantConfig.theme.branchColor
                )

                pos = newPos
            } else if (c == '+') {
                val rotChange = randomizer.nextFloatAround(1f, variability)
                angle += plantConfig.branches.angle.toDouble() * rotChange
            } else if (c == '-') {
                val rotChange = randomizer.nextFloatAround(1f, variability)
                angle -= plantConfig.branches.angle.toDouble() * rotChange
            } else if (c == '[') {
                stack.add(Triple(pos, angle, width))
            } else if (c == ']') {
                drawLeaf(this, plantConfig, randomizer, pos, angle, variability)
                if (stack.isNotEmpty()) {
                    val (p, a, w) = stack.removeAt(stack.size - 1)
                    pos = p
                    angle = a
                    width = w
                }
            }
        }
    }

    private fun drawLeaf(
        scope: DrawScope,
        config: PlantConfig,
        randomizer: Randomizer,
        pos: Offset,
        angle: Double,
        variability: Float
    ) {
        val leafWidth = randomizer.nextFloatAround(config.leaves.width, variability)
        val leafLength = randomizer.nextFloatAround(config.leaves.length, variability)
        val rad = toRadians(angle).toFloat()
        val cosr = cos(rad)
        val sinr = sin(rad)
        val leafColor = config.theme.leafColor.copy(alpha = config.theme.leafAlpha)

        fun rotateAndTranslate(ptX: Float, ptY: Float): Offset {
            val rx = ptX * cosr - ptY * sinr
            val ry = ptX * sinr + ptY * cosr
            return pos + Offset(rx, ry)
        }

        val path = Path()
        when (config.leaves.type) {
            LeafType.TYPE0 -> {
                val points = listOf(
                    0f to 0f, 1f to -1f, 0f to -4f, -1f to -1f, 0f to 0f
                )
                points.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * leafWidth, y * leafLength)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                scope.drawPath(path, leafColor)
            }

            LeafType.TYPE1 -> {
                (0..360 step 10).forEach { i ->
                    val theta = toRadians(i.toDouble()).toFloat()
                    val px = 2 * leafWidth * cos(theta)
                    val py = -2 * leafLength + 2 * leafLength * sin(theta)
                    val abs = rotateAndTranslate(px, py)
                    if (i == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                path.close()
                scope.drawPath(path, leafColor)
            }

            LeafType.TYPE2 -> {

                val points = listOf(
                    0f to 0f, 1f to -1f, 1f to -4f, 0f to -5f, -1f to -4f, -1f to -1f, 0f to 0f
                )
                points.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * leafWidth, y * leafLength)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                scope.drawPath(path, leafColor)

                path.reset()
                val rectPoints = listOf(
                    0f to 0f, 0.25f to 0f, 0.25f to -5f, 0f to -5f, 0f to 0f
                )
                rectPoints.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * leafWidth, y * leafLength)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
                scope.drawPath(path, leafColor)
            }
        }
    }

    private fun drawBranch() {

    }
}
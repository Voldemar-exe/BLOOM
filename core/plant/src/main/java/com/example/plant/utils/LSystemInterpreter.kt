package com.example.plant.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.plant.BranchConfig
import com.example.plant.LeafConfig
import com.example.plant.LeafType
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

private class UnknownLSystemCommand(override val message: String?) : Exception()

data class Branch(
    val id: Int = 0,
    val parentId: Int? = null,
    val parentPointIndex: Int? = null,
    val points: List<BranchPoint> = emptyList(),
    val order: Int = 1,
)

data class BranchPoint(
    val x: Float,
    val y: Float,
    val angle: Double,
    val radius: Float,
) {
    fun getOffset(): Offset = Offset(x, y)
}

data class LeafPrimitive(
    val position: Offset,
    val angle: Double,
    val length: Float,
    val type: LeafType,
    val branchId: Int,
)

data class PlantGeometry(
    val branches: List<Branch>,
    val leaves: List<LeafPrimitive>,
    val bounds: Rect,
)

interface LSystemInterpreter {
    fun generatePoints(
        lSystemSentence: String,
        offset: Offset,
        variability: Float,
        branchConfig: BranchConfig,
        leafConfig: LeafConfig,
        randomizer: Randomizer,
    ): PlantGeometry
}

class LSystemInterpreterImpl : LSystemInterpreter {
    override fun generatePoints(
        lSystemSentence: String,
        offset: Offset,
        variability: Float,
        branchConfig: BranchConfig,
        leafConfig: LeafConfig,
        randomizer: Randomizer,
    ): PlantGeometry {
        val branches = mutableListOf<Branch>()
        val leaves = mutableListOf<LeafPrimitive>()

        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        var pos = offset
        var angle = 90.0
        var width = branchConfig.baseWidth
        val falloff = 1 - branchConfig.widthFalloff

        var currentBranchId = 0

        var currentBranch = Branch()
        val currentBranchPoints = mutableListOf<BranchPoint>()

        currentBranchPoints.add(BranchPoint(pos.x, pos.y, angle, width))

        val stack = mutableListOf<Branch>()

        for (c in lSystemSentence) {
            when (c) {
                'F' -> {
                    val startWidth = width
                    val endWidth =
                        max(
                            startWidth * falloff.pow(0.1f) / currentBranch.order,
                            branchConfig.minWidth,
                        )
                    width = endWidth
                    val lengthChange = randomizer.nextFloatAround(1f, variability)
                    val length = branchConfig.baseLength * lengthChange
                    val rad = toRadians(angle).toFloat()
                    val dx = length * cos(rad)
                    val dy = -length * sin(rad)
                    val newPos = pos + Offset(dx, dy)

                    minX = min(minX, newPos.x)
                    minY = min(minY, newPos.y)
                    maxX = max(maxX, newPos.x)
                    maxY = max(maxY, newPos.y)

                    currentBranchPoints.add(BranchPoint(newPos.x, newPos.y, angle, width))

                    pos = newPos
                }

                '+' -> {
                    val rotChange = randomizer.nextFloatAround(1f, variability)
                    angle += branchConfig.baseAngle.toDouble() * rotChange
                }

                '-' -> {
                    val rotChange = randomizer.nextFloatAround(1f, variability)
                    angle -= branchConfig.baseAngle.toDouble() * rotChange
                }

                '[' -> {
                    stack.add(currentBranch.copy(points = currentBranchPoints.toList()))

                    currentBranchId += 1
                    currentBranchPoints.clear()

                    currentBranch =
                        Branch(
                            id = currentBranchId,
                            parentId = stack.last().id,
                            parentPointIndex = stack.last().points.size - 1,
                            order = stack.size,
                        )
                }

                ']' -> {
                    if (currentBranchPoints.isNotEmpty()) {
                        branches.add(currentBranch.copy(points = currentBranchPoints.toList()))
                    }

                    if (stack.isNotEmpty()) {
                        currentBranch = stack.removeAt(stack.size - 1)
                        currentBranchPoints.clear()
                        currentBranchPoints.addAll(currentBranch.points)
                        if (currentBranchPoints.isNotEmpty()) {
                            val (x, y, a, w) = currentBranchPoints[currentBranchPoints.size - 1]
                            pos = Offset(x, y)
                            angle = a
                            width = w
                        }
                    }
                }

                'X' -> {
                    val leafLength =
                        randomizer.nextFloatAround(leafConfig.length, variability)

                    leaves.add(
                        LeafPrimitive(
                            position = pos,
                            angle = angle,
                            length = leafLength,
                            type = leafConfig.type,
                            currentBranch.id,
                        ),
                    )
                }

                else -> {
                    throw UnknownLSystemCommand("Command $c")
                }
            }
        }

        if (currentBranchPoints.isNotEmpty()) {
            branches.add(currentBranch.copy(points = currentBranchPoints.toList()))
        }

        val bounds =
            Rect(
                minX - (leafConfig.length + variability),
                minY - (leafConfig.length + variability),
                maxX + (leafConfig.length + variability),
                maxY,
            )

        return PlantGeometry(branches, leaves, bounds)
    }
}

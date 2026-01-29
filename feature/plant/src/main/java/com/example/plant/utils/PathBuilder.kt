package com.example.plant.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.example.plant.BranchConfig
import com.example.plant.LeafType
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

data class BranchPath(
    val id: Int,
    val path: Path,
    val parentId: Int?,
    val order: Int
)

data class LeafPath(
    val path: Path
)

data class PlantPaths(
    val branchesPaths: List<BranchPath>,
    val leavesPaths: List<LeafPath>
)

interface PathBuilder {
    fun buildPlant(
        geometry: PlantGeometry,
        branchConfig: BranchConfig
    ): PlantPaths
}

class PathBuilderImpl : PathBuilder {
    override fun buildPlant(
        geometry: PlantGeometry,
        branchConfig: BranchConfig
    ): PlantPaths {
        buildBranchHierarchyCache(geometry.branches)

        val branches: List<BranchPath> = geometry.branches.map { branch ->
            val allBranchPoints =
                buildFullPointList(branch, branch.parentId?.let { getBranch(it) })

            val branchPath = buildBranchPath(allBranchPoints)

            BranchPath(
                id = branch.id,
                path = branchPath,
                parentId = branch.parentId,
                order = branch.order
            )
        }

        val leaves: List<LeafPath> = geometry.leaves.map { buildLeafPath(it) }

        return PlantPaths(
            branchesPaths = branches,
            leavesPaths = leaves
        )
    }

    private fun buildFullPointList(
        branch: Branch,
        parent: Branch?
    ): List<BranchPoint> {
        return if (parent != null && branch.parentPointIndex != null) {
            val parentPoint = parent.points[branch.parentPointIndex]
            /*val parentPointForLayering =
                if (branch.parentPointIndex > 0) parent.points[branch.parentPointIndex - 1]
                else parentPoint*/

            val childStartPoint = parentPoint.copy(angle = branch.points.first().angle)

            if (branch.points.size < 2) return listOf(
                parentPoint,
                childStartPoint,
            ) + branch.points

            if (branch.points.size > 5) return listOf(
                parentPoint,
                childStartPoint
            ) + branch.points.drop(2)

            listOf(parentPoint, childStartPoint) + branch.points.drop(1)
        } else {
            branch.points
        }
    }

    fun buildBranchPath(
        points: List<BranchPoint>
    ): Path {
        if (points.isEmpty()) return Path()

        val leftBoundary = mutableListOf<Offset>()
        val rightBoundary = mutableListOf<Offset>()

        points.forEach { point ->
            val center = point.getOffset()
            val radius = point.radius

            val angle: Float = toRadians(point.angle).toFloat()

            val normalX = cos(angle + Math.PI / 2).toFloat()
            val normalY = -sin(angle + Math.PI / 2).toFloat()

            val leftPoint = Offset(
                center.x + normalX * radius,
                center.y + normalY * radius
            )

            val rightPoint = Offset(
                center.x - normalX * radius,
                center.y - normalY * radius
            )

            leftBoundary += leftPoint
            rightBoundary += rightPoint
        }

        return Path().apply {
            moveTo(leftBoundary.first().x, leftBoundary.first().y)

            for (i in 1 until leftBoundary.size) {
                lineTo(leftBoundary[i].x, leftBoundary[i].y)
            }

            for (i in rightBoundary.size - 1 downTo 0) {
                lineTo(rightBoundary[i].x, rightBoundary[i].y)
            }
            close()
        }
    }

    private val branchById = mutableMapOf<Int, Branch>()
    private val branchChildrenCache = mutableMapOf<Int?, List<Branch>>()

    private fun buildBranchHierarchyCache(branches: List<Branch>) {
        branchById.clear()
        branchChildrenCache.clear()

        branches.forEach { branch ->
            branchById[branch.id] = branch
        }

        branches.groupBy { it.parentId }
            .forEach { (parentId, children) ->
                branchChildrenCache[parentId] = children.sortedBy { it.order }
            }
    }

    fun getChildren(parentId: Int?): List<Branch> =
        branchChildrenCache[parentId] ?: emptyList()

    fun getBranch(id: Int): Branch? = branchById[id]

    fun buildLeafPath(leaf: LeafPrimitive): LeafPath {
        val rad = toRadians(leaf.angle).toFloat()
        val cosr = cos(rad)
        val sinr = -sin(rad)

        val width = 0.75f * leaf.length

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
                    val abs = rotateAndTranslate(x * width, y * leaf.length)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
            }

            LeafType.TYPE1 -> {
                (0..360 step 10).forEach { i ->
                    val theta = toRadians(i.toDouble()).toFloat()
                    val px = 2 * width * cos(theta)
                    val py = -2 * leaf.length + 2 * leaf.length * sin(theta)
                    val abs = rotateAndTranslate(px, py)
                    if (i == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }
            }

            LeafType.TYPE2 -> {

                val points = listOf(
                    0f to 0f, 1f to -1f, 1f to -4f, 0f to -5f, -1f to -4f, -1f to -1f, 0f to 0f
                )
                points.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * width, y * leaf.length)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }

                /*path.reset()
                val rectPoints = listOf(
                    0f to 0f, 0.25f to 0f, 0.25f to -5f, 0f to -5f, 0f to 0f
                )
                rectPoints.forEachIndexed { index, (x, y) ->
                    val abs = rotateAndTranslate(x * width, y * leaf.length)
                    if (index == 0) path.moveTo(abs.x, abs.y)
                    else path.lineTo(abs.x, abs.y)
                }*/
            }
        }

        return LeafPath(path)
    }
}
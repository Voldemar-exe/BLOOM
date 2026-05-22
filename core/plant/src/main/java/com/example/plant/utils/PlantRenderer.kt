package com.example.plant.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.plant.RenderConfig

interface PlantRenderer {
    fun drawPlant(
        drawScope: DrawScope,
        plantPaths: PlantPaths,
        renderConfig: RenderConfig,
        progress: Float,
    )

    fun drawBranch(
        drawScope: DrawScope,
        branchPath: BranchPath,
        color: Color,
    )

    fun drawLeaf(
        drawScope: DrawScope,
        leaf: LeafPath,
        color: Color,
    )
}

class PlantRendererImpl : PlantRenderer {
    override fun drawPlant(
        drawScope: DrawScope,
        plantPaths: PlantPaths,
        renderConfig: RenderConfig,
        progress: Float,
    ) {
        val total = plantPaths.branchesPaths.size
        if (total == 0) return

        val branchesToDraw =
            if (progress >= 1.0f) {
                total
            } else {
                (progress * total).toInt().coerceIn(0, total - 1)
            }

        val branchPath = Path()
        val drawnBranchIds = mutableSetOf<Int>()

        for ((index, branch) in plantPaths.branchesPaths.withIndex()) {
            if (index < branchesToDraw) {
                branchPath.addPath(branch.path)
                drawnBranchIds.add(branch.id)
            }
        }
        drawScope.drawPath(branchPath, renderConfig.branchColor)

        val leafPath = Path()
        for (leaf in plantPaths.leavesPaths) {
            if (leaf.branchId in drawnBranchIds) {
                leafPath.addPath(leaf.path)
            }
        }
        drawScope.drawPath(leafPath, renderConfig.leafColor)
    }

    private fun drawPlant(
        drawScope: DrawScope,
        plantPaths: PlantPaths,
        renderConfig: RenderConfig,
    ) {
        val path = Path()
        val leafPath = Path()

        for (branch in plantPaths.branchesPaths.sortedBy { it.order }) {
            path.addPath(branch.path)
        }

        drawScope.drawPath(path, renderConfig.branchColor)

        for (leaf in plantPaths.leavesPaths) {
            leafPath.addPath(leaf.path)
        }

        drawScope.drawPath(leafPath, renderConfig.leafColor)
    }

    override fun drawBranch(
        drawScope: DrawScope,
        branchPath: BranchPath,
        color: Color,
    ) {
        drawScope.drawPath(branchPath.path, color)
    }

    override fun drawLeaf(
        drawScope: DrawScope,
        leaf: LeafPath,
        color: Color,
    ) {
        drawScope.drawPath(leaf.path, color)
    }

    private fun drawPathAsPoints(
        drawScope: DrawScope,
        path: Path,
        radius: Float = 2f,
    ) {
        for (segment in path) {
            val type = segment.type

            for (i in 0 until segment.points.size step 2) {
                val x = segment.points[i]
                val y = segment.points[i + 1]
                val point = Offset(x, y)

                drawScope.drawCircle(
                    color = Color.Red,
                    radius = radius,
                    center = point,
                )
            }
        }
    }
}

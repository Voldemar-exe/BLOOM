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
        renderConfig: RenderConfig
    )

    fun drawBranch(
        drawScope: DrawScope,
        branchPath: BranchPath,
        color: Color
    )

    fun drawLeaf(
        drawScope: DrawScope,
        leaf: LeafPath,
        color: Color
    )
}

class PlantRendererImpl : PlantRenderer {

    override fun drawPlant(
        drawScope: DrawScope,
        plantPaths: PlantPaths,
        renderConfig: RenderConfig
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

        // TESTING
        /*for (branch in plantPaths.branchesPaths.sortedBy { it.order }) {
            drawBranchAsPoints(drawScope, branch)
        }*/

        /*for (branches in plantPaths.branchesPaths.groupBy { it.order }) {
            val branchPath = Path()
            branches.value.forEach { branch ->
                branchPath.addPath(branch.path)
            }
            drawScope.drawPath(branchPath, renderConfig.branchColor)
        }*/

        /*val colors = listOf(
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.Magenta,
            Color.Cyan
        )

        for (branch in plantPaths.branchesPaths.sortedBy { it.order }) {
            val color = colors.getOrNull(branch.order) ?: Color.Black
            drawBranch(drawScope, branch, color)
        }*/
    }

    override fun drawBranch(
        drawScope: DrawScope,
        branchPath: BranchPath,
        color: Color
    ) {
        drawScope.drawPath(branchPath.path, color)
    }

    private fun drawBranchAsPoints(
        drawScope: DrawScope,
        branchPath: BranchPath
    ) {
        val path = branchPath.path

        for (segment in path) {
            val type = segment.type

            for (i in 0 until segment.points.size step 2) {
                val x = segment.points[i]
                val y = segment.points[i + 1]
                val point = Offset(x, y)

                drawScope.drawCircle(
                    color = Color.Red,
                    radius = 2f,
                    center = point
                )
            }
        }
    }

    override fun drawLeaf(
        drawScope: DrawScope,
        leaf: LeafPath,
        color: Color
    ) {
        drawScope.drawPath(leaf.path, color)
    }
}
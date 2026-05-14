package com.example.plant.utils

import android.graphics.PathMeasure
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
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
        val branchPath = Path()
        val leafPath = Path()

        val total = plantPaths.branchesPaths.size
        val global = progress * total

        for (branch in plantPaths.branchesPaths.sortedBy { it.id }) {
            val local = (global - branch.id).coerceIn(0f, 1f)
            val eased = FastOutSlowInEasing.transform(local)

            branchPath.addPath(branch.path.trimToProgress(eased))
        }

        drawScope.drawPath(branchPath, renderConfig.branchColor)

        for (leaf in plantPaths.leavesPaths.sortedBy { it.branchId }) {
            val local = (global - leaf.branchId).coerceIn(0f, 1f)
            val eased = LinearOutSlowInEasing.transform(local)

            leafPath.addPath(leaf.path.trimToProgress(eased))
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

private fun Path.trimToProgress(progress: Float): Path {
    val androidPath = this.asAndroidPath()
    val measure = PathMeasure(androidPath, false)

    val length = measure.length
    val outPath = android.graphics.Path()

    val stop = length * progress

    measure.getSegment(0f, stop, outPath, true)

    return outPath.asComposePath()
}

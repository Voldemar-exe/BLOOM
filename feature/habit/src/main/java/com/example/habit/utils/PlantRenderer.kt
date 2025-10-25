package com.example.habit.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.habit.models.Command
import kotlin.math.cos
import kotlin.math.sin


interface IPlantRenderer {
    fun drawPlant(
        drawScope: DrawScope,
        commands: List<Command>,
        offset: Offset,
        progress: Float,
        cellSize: Size
    ): DrawScope
}

class DefaultPlantRenderer : IPlantRenderer {
    override fun drawPlant(
        drawScope: DrawScope,
        commands: List<Command>,
        offset: Offset,
        progress: Float,
        cellSize: Size
    ) = drawScope.apply {
        val maxDimension = minOf(cellSize.width, cellSize.height)
        val scale = maxDimension / 100f

        var position = offset
        var angle = -90f
        val stack = mutableListOf<Pair<Offset, Float>>()

        val stemColor = Color(0xFF3D6B3D)
        val petalColor = Color(0xFFFF9A9E)
        val centerColor = Color(0xFFFFD166)

        val strokeWidth = scale

        val commandsToDraw = commands.take((commands.size * progress).toInt())

        for (command in commandsToDraw) {
            when (command.type) {
                Command.Type.FORWARD -> {
                    val scaledLength = command.length * scale
                    val radians = angle * (Math.PI / 180)
                    val endX = position.x + scaledLength * cos(radians).toFloat()
                    val endY = position.y + scaledLength * sin(radians).toFloat()
                    val endPos = Offset(endX, endY)

                    val color = if (scaledLength < 1.5f * scale) {
                        if (scaledLength < 1f * scale) centerColor else petalColor
                    } else {
                        stemColor
                    }

                    drawLine(
                        color = color,
                        start = position,
                        end = endPos,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )

                    position = endPos
                }

                Command.Type.TURN_LEFT -> angle += command.angle
                Command.Type.TURN_RIGHT -> angle -= command.angle
                Command.Type.PUSH -> stack.add(position to angle)
                Command.Type.POP -> {
                    val (savedPos, savedAngle) = stack.removeLastOrNull() ?: continue
                    position = savedPos
                    angle = savedAngle
                }
            }
        }
    }
}
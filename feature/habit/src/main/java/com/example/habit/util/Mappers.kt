package com.example.habit.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import com.example.model.HabitPlant
import com.example.plant.BranchConfig
import com.example.plant.LeafConfig
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import com.example.plant.RenderConfig
import com.example.plant.utils.PresetExample

// TODO: Maybe replace config with new model
fun HabitPlant.toPlantConfig(): PlantConfig =
    PlantConfig(
        lSystemSentence =
            generateSentence(
                seed = seed,
                presetId = presetId,
                iterations = iterations,
                variability = variability,
            ),
        branchConfig =
            BranchConfig(
                baseLength = baseLength,
                baseAngle = baseAngle,
                baseWidth = baseWidth,
                widthFalloff = widthFalloff,
                minWidth = widthFalloffEndAt,
            ),
        leafConfig =
            LeafConfig(
                length = petalLength,
                type = LeafType.valueOf(petalType),
            ),
        renderConfig =
            RenderConfig(
                branchColor = Color.fromColorLong(baseColor),
                leafColor = Color.fromColorLong(petalColor),
                leafAlpha = petalAlpha,
            ),
    )

fun PresetExample.toHabitPlant(): HabitPlant {
    val config =
        makePlantConfig(
            generateSentence(
                generationConfig.seed,
                generationConfig.presetId,
                generationConfig.iterations,
                generationConfig.variability,
            ),
        )

    return HabitPlant(
        id = 0,
        habitId = 0,
        presetId = generationConfig.presetId,
        iterations = generationConfig.iterations,
        variability = generationConfig.variability,
        seed = generationConfig.seed,
        baseAngle = config.branchConfig.baseAngle,
        baseLength = config.branchConfig.baseLength,
        baseWidth = config.branchConfig.baseWidth,
        widthFalloff = config.branchConfig.widthFalloff,
        widthFalloffEndAt = config.branchConfig.minWidth,
        petalLength = config.leafConfig.length,
        petalType = config.leafConfig.type.name,
        petalColor =
            config.renderConfig.leafColor.value
                .toLong(),
        baseColor =
            config.renderConfig.branchColor.value
                .toLong(),
        petalAlpha = config.renderConfig.leafAlpha,
    )
}

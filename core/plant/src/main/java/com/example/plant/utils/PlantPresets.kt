package com.example.plant.utils

import androidx.compose.ui.graphics.Color
import com.example.plant.BranchConfig
import com.example.plant.GenerationConfig
import com.example.plant.LeafConfig
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import com.example.plant.RenderConfig

data class PresetExample(
    val id: Int,
    val label: String,
    val generationConfig: GenerationConfig,
    val makePlantConfig: (sentence: String) -> PlantConfig,
)

private fun parseColor(hex: String): Color {
    val colorLong = hex.removePrefix("#").toLong(16)
    return if (hex.length == 7) {
        Color((colorLong or 0xFF000000L).toInt())
    } else {
        Color(colorLong.toInt())
    }
}

object PresetLibrary {
    fun getExamples(): List<PresetExample> =
        listOf(
            PresetExample(
                id = 0,
                label = "Sakura",
                generationConfig =
                    GenerationConfig(
                        presetId = 0,
                        iterations = 4,
                        variability = 0.55f,
                        seed = 65,
                    ),
                makePlantConfig = { sentence ->
                    PlantConfig(
                        lSystemSentence = sentence,
                        leafConfig = LeafConfig(type = LeafType.TYPE0, length = 10f),
                        branchConfig =
                            BranchConfig(
                                baseLength = 15.5f,
                                baseAngle = 27.1f,
                                baseWidth = 38.8f,
                                widthFalloff = 0.56f,
                                minWidth = 2.7f,
                            ),
                        renderConfig =
                            RenderConfig(
                                branchColor = Color(0xFF4E342E),
                                leafColor = Color(0xFFFF8FE7),
                                leafAlpha = 0.82f,
                            ),
                    )
                },
            ),
            PresetExample(
                id = 1,
                label = "Simple Tree",
                generationConfig =
                    GenerationConfig(
                        presetId = 1,
                        iterations = 5,
                        variability = 0.0f,
                        seed = 123,
                    ),
                makePlantConfig = { sentence ->
                    PlantConfig(
                        lSystemSentence = sentence,
                        leafConfig = LeafConfig(type = LeafType.TYPE1, length = 8f),
                        branchConfig =
                            BranchConfig(
                                baseLength = 12f,
                                baseAngle = 25f,
                                baseWidth = 30f,
                                widthFalloff = 0.62f,
                                minWidth = 2.0f,
                            ),
                        renderConfig =
                            RenderConfig(
                                branchColor = Color(0xFF6D4C41),
                                leafColor = Color(0xFF7CB342),
                                leafAlpha = 0.88f,
                            ),
                    )
                },
            ),
            PresetExample(
                id = 2,
                label = "Binary Tree",
                generationConfig =
                    GenerationConfig(
                        presetId = 2,
                        iterations = 6,
                        variability = 0.0f,
                        seed = 456,
                    ),
                makePlantConfig = { sentence ->
                    PlantConfig(
                        lSystemSentence = sentence,
                        leafConfig = LeafConfig(type = LeafType.TYPE2, length = 12f),
                        branchConfig =
                            BranchConfig(
                                baseLength = 20f,
                                baseAngle = 30f,
                                baseWidth = 40f,
                                widthFalloff = 0.5f,
                                minWidth = 3.0f,
                            ),
                        renderConfig =
                            RenderConfig(
                                branchColor = Color(0xFF5D4037),
                                leafColor = Color(0xFF43A047),
                                leafAlpha = 0.9f,
                            ),
                    )
                },
            ),
            PresetExample(
                id = 3,
                label = "Fern-like",
                generationConfig =
                    GenerationConfig(
                        presetId = 3,
                        iterations = 4,
                        variability = 0.0f,
                        seed = 789,
                    ),
                makePlantConfig = { sentence ->
                    PlantConfig(
                        lSystemSentence = sentence,
                        leafConfig = LeafConfig(type = LeafType.TYPE0, length = 6f),
                        branchConfig =
                            BranchConfig(
                                baseLength = 10f,
                                baseAngle = 22f,
                                baseWidth = 25f,
                                widthFalloff = 0.65f,
                                minWidth = 1.5f,
                            ),
                        renderConfig =
                            RenderConfig(
                                branchColor = Color(0xFF8D6E63),
                                leafColor = Color(0xFF66BB6A),
                                leafAlpha = 0.8f,
                            ),
                    )
                },
            ),
        )
}

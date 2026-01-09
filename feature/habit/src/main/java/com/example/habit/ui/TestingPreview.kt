package com.example.habit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habit.models.PlantState
import com.example.plant.BranchConfig
import com.example.plant.GenerationConfig
import com.example.plant.LeafConfig
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import com.example.plant.PlantTheme
import com.example.plant.utils.LSystemGenerator
import com.example.plant.utils.Randomizer

fun parseColor(hex: String): Color {
    val colorLong = hex.removePrefix("#").toLong(16)
    return if (hex.length == 7) Color((colorLong or 0xFF000000L).toInt())
    else Color(colorLong.toInt())
}

// Example usage in a Composable
@Composable
fun DefaultLSystemView() {
    val generationConfig = GenerationConfig(
        presetIndex = 0,
        iterations = 4,
        variability = 0.55f,
        seed = 65
    )

    val generator = LSystemGenerator(Randomizer(generationConfig.seed))
    val sentence = generator.generateSentence(
        generationConfig.presetIndex,
        generationConfig.iterations,
        generationConfig.variability
    )

    val config = PlantConfig(
        lSystemSentence = sentence,
        leaves = LeafConfig(
            type = LeafType.TYPE2,
            length = 8.5f,
            width = 12.5f
        ),
        branches = BranchConfig(
            length = 15.5f,
            angle = 27.1f,
            width = 38.8f,
            widthFalloff = 0.56f,
            minWidth = 2.7f
        ),
        theme = PlantTheme(
            branchColor = parseColor("#504534"),
            leafColor = parseColor("#ff8fe7"),
            leafAlpha = 0.76f
        ),
        isAnimated = false,
    )

    Scaffold { paddingValues ->
        Row(Modifier.padding(paddingValues), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PlantItem(
                modifier = Modifier.weight(1f),
                randomizer = Randomizer(generationConfig.seed),
                variability = generationConfig.variability,
                plantState = PlantState(
                    lSystemSentence = sentence,
                    label = "Example",
                    plantConfig = config
                ),
                onAnimate = {},
                onNextStage = {}
            )

            PlantItem(
                modifier = Modifier.weight(1f),
                randomizer = Randomizer(generationConfig.seed),
                variability = generationConfig.variability,
                plantState = PlantState(
                    lSystemSentence = sentence,
                    label = "Example",
                    plantConfig = config
                ),
                onAnimate = {},
                onNextStage = {}
            )
        }
    }


}
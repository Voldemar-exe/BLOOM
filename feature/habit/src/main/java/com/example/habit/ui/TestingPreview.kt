package com.example.habit.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        seed = 27
    )
    val randomizer = Randomizer(generationConfig.seed)

    val generator = LSystemGenerator(randomizer)
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
    PlantCanvas(randomizer, generationConfig.variability, config, Modifier.fillMaxSize())

//    PlantItem(
//        modifier = Modifier.fillMaxSize(),
//        randomizer = randomizer,
//        variability = generationConfig.variability,
//        plantState = PlantState(
//            lSystemSentence = sentence,
//            label = "Example",
//            plantConfig = config
//        ),
//        onAnimate = {},
//        onNextStage = {}
//    )
}
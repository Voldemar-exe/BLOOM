package com.example.habit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.habit.models.PlantState
import com.example.habit.ui.parseColor
import com.example.plant.BranchConfig
import com.example.plant.GenerationConfig
import com.example.plant.LeafConfig
import com.example.plant.LeafType
import com.example.plant.PlantConfig
import com.example.plant.PlantTheme
import com.example.plant.utils.LSystemGenerator
import com.example.plant.utils.Randomizer

class PlantViewModel(
//    private val renderer: PlantRenderer
) : ViewModel() {
    private val _plants = mutableStateOf<List<PlantState>>(emptyList())
    val plants get() = _plants

    init {
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

        val plantConfig = PlantConfig(
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

        _plants.value = listOf<PlantState>(
            PlantState(
                lSystemSentence = sentence,
                label = "Example",
                id = 0,
                plantConfig = plantConfig,
                generationConfig = generationConfig
            ),
            PlantState(
                lSystemSentence = sentence,
                label = "Example",
                id = 1,
                plantConfig = plantConfig,
                generationConfig = generationConfig
            )
        )
    }
}
package com.example.habit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.habit.models.PlantState
import com.example.plant.utils.LSystemGeneratorImpl
import com.example.plant.utils.PresetLibrary
import com.example.plant.utils.Randomizer

class PlantViewModel : ViewModel() {
    private val _plants = mutableStateOf<List<PlantState>>(emptyList())
    val plants get() = _plants

    init {
        _plants.value =
            PresetLibrary.getExamples().map { (id, label, generationConfig, makePlantConfig) ->
                val generator = LSystemGeneratorImpl(Randomizer(generationConfig.seed))
                val sentence = generator.generateSentence(
                    generationConfig.presetId,
                    generationConfig.iterations,
                    generationConfig.variability
                )
                PlantState(
                    lSystemSentence = sentence,
                    label = label,
                    id = id,
                    plantConfig = makePlantConfig(sentence),
                    generationConfig = generationConfig
                )
            }
    }
}
package com.example.habit

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.habit.PlantState
import com.example.plant.LeafType
import com.example.plant.utils.LSystemGeneratorImpl
import com.example.plant.utils.PresetLibrary
import com.example.plant.utils.Randomizer
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import kotlin.random.Random

class PlantViewModel : ViewModel() {
    private val _plants = MutableStateFlow<List<PlantState>>(emptyList())
    val plants get() = _plants

    init {
        _plants.value =
            PresetLibrary.getExamples().map { (id, label, generationConfig, makePlantConfig) ->
                val generator = LSystemGeneratorImpl(Randomizer(generationConfig.seed))
                val sentence =
                    generator.generateSentence(
                        generationConfig.presetId,
                        generationConfig.iterations,
                        generationConfig.variability,
                    )
                Timber.i("${sentence.length} : $sentence")
                PlantState(
                    lSystemSentence = sentence,
                    label = label,
                    id = id,
                    plantConfig = makePlantConfig(sentence),
                    generationConfig = generationConfig,
                )
            }
    }

    fun changeAllVariability() {
        _plants.value =
            _plants.value.map { plant ->
                plant.copy(
                    generationConfig =
                        plant.generationConfig.copy(
                            variability = Random.nextFloat(),
                        ),
                )
            }
    }

    fun changePreset() {
        val presets = PresetLibrary.getExamples()

        _plants.value =
            _plants.value.map { plant ->
                val randomPreset = presets.random()

                val (id, label, generationConfig, makePlantConfig) = randomPreset

                val generator = LSystemGeneratorImpl(Randomizer(generationConfig.seed))
                val sentence =
                    generator.generateSentence(
                        generationConfig.presetId,
                        generationConfig.iterations,
                        generationConfig.variability,
                    )

                plant.copy(
                    label = label,
                    lSystemSentence = sentence,
                    generationConfig = generationConfig,
                    plantConfig = makePlantConfig(sentence),
                )
            }
    }

    fun randomizeLeafTypes() {
        _plants.value =
            _plants.value.map { plant ->
                plant.copy(
                    plantConfig =
                        plant.plantConfig.copy(
                            leafConfig =
                                plant.plantConfig.leafConfig.copy(
                                    type = LeafType.entries.random(),
                                ),
                        ),
                )
            }
    }

    fun randomizeColors() {
        _plants.value =
            _plants.value.map { plant ->
                plant.copy(
                    plantConfig =
                        plant.plantConfig.copy(
                            renderConfig =
                                plant.plantConfig.renderConfig.copy(
                                    branchColor = randomColor(),
                                    leafColor = randomColor(),
                                ),
                        ),
                )
            }
    }

    private fun randomColor(): Color =
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f,
        )
}

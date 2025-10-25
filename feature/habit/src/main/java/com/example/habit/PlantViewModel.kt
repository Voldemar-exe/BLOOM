package com.example.habit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.models.PlantState
import com.example.habit.utils.LSystemGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.apply
import kotlin.collections.map
import kotlin.collections.toMutableList

class PlantViewModel() : ViewModel() {
    private val _plants = mutableStateOf<List<PlantState>>(emptyList())
    val plants = _plants

    private val generator = LSystemGenerator()

    fun initializePlants(count: Int) {
        val newPlants = (1..count).map { i ->

            val (commands, params) = generator.generateWithRandomParams()

            val stages = (1..params.iterations).map { iter ->
                generator.generateWithRandomParams()
            }

            PlantState(
                commands = commands,
                label = "Растение $i (${params.iterations} ит.)",
                growthStages = stages.map { it.first },
                currentStage = 0
            )
        }
        _plants.value = newPlants
    }

    fun startAnimation(index: Int) {
        val plant = _plants.value[index]
        if (!plant.isAnimating) {
            viewModelScope.launch {
                _plants.value = _plants.value.toMutableList().apply {
                    this[index] = plant.copy(isAnimating = true, progress = 0f)
                }
                for (i in 0..100) {
                    delay(20)
                    _plants.value = _plants.value.toMutableList().apply {
                        val updated = this[index].copy(progress = i / 100f)
                        this[index] = updated
                    }
                }
                _plants.value = _plants.value.toMutableList().apply {
                    this[index] = this[index].copy(isAnimating = false)
                }
            }
        }
    }

    fun nextGrowthStage(index: Int) {
        val plant = _plants.value[index]
        val nextStage = (plant.currentStage + 1) % plant.growthStages.size
        val newCommands = plant.growthStages[nextStage]
        _plants.value = _plants.value.toMutableList().apply {
            this[index] = plant.copy(
                currentStage = nextStage,
                commands = newCommands,
                progress = 1f
            )
        }
    }
}
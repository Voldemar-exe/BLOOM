package com.example.habit.util

import com.example.plant.utils.LSystemGeneratorImpl
import com.example.plant.utils.Randomizer

fun generateSentence(
    seed: Long,
    presetId: Int,
    iterations: Int,
    variability: Float,
): String =
    LSystemGeneratorImpl(Randomizer(seed)).generateSentence(
        presetId,
        iterations,
        variability,
    )

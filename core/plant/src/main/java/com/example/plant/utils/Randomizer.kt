package com.example.plant.utils

import kotlin.random.Random

class Randomizer(seed: Long) {
    private val random = Random(seed)
    fun nextFloat(): Float = random.nextFloat()
    fun nextFloatRange(min: Float, max: Float): Float = min + (max - min) * nextFloat()
    fun nextFloatAround(base: Float, variability: Float): Float {
        val min = base * (1 - variability)
        val max = base * (1 + variability)
        return min + (max - min) * nextFloat()
    }
}
package com.example.designsystem.picture

import com.example.bloom.core.designsystem.R

enum class BloomPlants {
    SAKURA,
    SIMPLE,
    BINARY,
    ;

    companion object {
        val DEFAULT_KEY = SAKURA.name

        fun resolve(plantKey: String): Int =
            when (valueOf(plantKey)) {
                SAKURA -> R.drawable.plant_sakura
                SIMPLE -> R.drawable.plant_simple
                BINARY -> R.drawable.plant_binary
            }
    }
}

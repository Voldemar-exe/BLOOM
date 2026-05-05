package com.example.designsystem.picture

enum class BloomPlants {
    SAKURA,
    SIMPLE_TREE,
    BINARY_TREE,
    ;

    companion object {
        val DEFAULT_KEY = SAKURA.name

        fun resolve(plantKey: String): Int =
            when (valueOf(plantKey)) {
                SAKURA -> 0
                SIMPLE_TREE -> 1
                BINARY_TREE -> 2
            }
    }
}

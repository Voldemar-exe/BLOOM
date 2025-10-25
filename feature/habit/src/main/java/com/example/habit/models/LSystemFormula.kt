package com.example.habit.models

data class LSystemFormula(
    val axiom: String,
    val rules: Map<Char, String>,
    val defaultAngle: Float
)
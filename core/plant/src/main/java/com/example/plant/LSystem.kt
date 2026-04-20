package com.example.plant

data class LSystemFormula(
    val axiom: String,
    val rules: List<Rule>
)

data class Rule(
    val probability: Double,
    val charToReplace: Char,
    val replacingChars: String
)
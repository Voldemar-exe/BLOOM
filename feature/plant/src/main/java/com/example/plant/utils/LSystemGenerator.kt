package com.example.plant.utils

import com.example.plant.LSystemFormula
import com.example.plant.Rule

interface LSystemGenerator {
    fun generateSentence(
        presetId: Int,
        iterations: Int,
        variability: Float,
    ): String
}

class LSystemGeneratorImpl(
    private val randomizer: Randomizer,
) : LSystemGenerator {
    fun selectRule(matchingRules: List<Rule>): Rule? {
        val chance = randomizer.nextFloat()
        var total = 0.0
        for (rule in matchingRules) {
            total += rule.probability
            if (chance < total) return rule
        }
        return matchingRules.lastOrNull()
    }

    fun applyRulesToSentence(
        rules: List<Rule>,
        sentence: String,
    ): String {
        val newSentence = StringBuilder()
        for (c in sentence) {
            val matchingRules = rules.filter { it.charToReplace == c }
            if (matchingRules.isNotEmpty()) {
                val selectedRule = selectRule(matchingRules)
                newSentence.append(selectedRule?.replacingChars ?: "")
            } else {
                newSentence.append(c)
            }
        }
        return newSentence.toString()
    }

    override fun generateSentence(
        presetId: Int,
        iterations: Int,
        variability: Float, // TODO: Maybe add chances
    ): String {
        val preset = presets[presetId]
        var sentence = preset.axiom
        val rules = preset.rules
        repeat(iterations) {
            sentence = applyRulesToSentence(rules, sentence)
        }
        return sentence
    }

    companion object {
        val presets =
            listOf(
                LSystemFormula(
                    axiom = "X",
                    rules =
                        listOf(
                            Rule(1.0, 'F', "FF"),
                            Rule(1.0, 'X', "F+[-F-XF-X][+FF][--XF[+X]][++F-X]"),
                        ),
                ),
                LSystemFormula(
                    axiom = "X",
                    rules =
                        listOf(
                            Rule(1.0, 'F', "FF"),
                            Rule(1.0, 'X', "F[+X]F[-X]+X"),
                        ),
                ),
                LSystemFormula(
                    axiom = "X",
                    rules =
                        listOf(
                            Rule(1.0, 'F', "FF"),
                            Rule(1.0, 'X', "F[+X][-X]FX"),
                        ),
                ),
                LSystemFormula(
                    axiom = "X",
                    rules =
                        listOf(
                            Rule(1.0, 'F', "FF"),
                            Rule(1.0, 'X', "F-[+X]+F[+FX]-X"), // [X]
                        ),
                ),
            )
    }
}

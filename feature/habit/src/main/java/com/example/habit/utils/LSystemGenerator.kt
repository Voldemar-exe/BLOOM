package com.example.habit.utils

import com.example.habit.models.Command
import com.example.habit.models.LSystemFormula
import kotlin.random.Random
import kotlin.text.iterator

data class LSystemParams(
    val angle: Float,
    val iterations: Int,
    val initialLength: Float,
    val lengthReduction: Float
)

class LSystemGenerator(
    private val formulas: List<LSystemFormula> = defaultFormulas,
    private val angleRange: Pair<Float, Float> = 15f to 35f,
    private val iterationsRange: Pair<Int, Int> = 2 to 5,
    private val initialLengthRange: Pair<Float, Float> = 6f to 14f,
    private val lengthReductionRange: Pair<Float, Float> = 0.5f to 0.7f
) {
    fun generateWithRandomParams(): Pair<List<Command>, LSystemParams> {
        val formula = formulas.random()
        val angle =
            Random.nextFloat() * (angleRange.second - angleRange.first) + angleRange.first
        val iterations =
            Random.nextInt(iterationsRange.first, iterationsRange.second + 1)
        val initialLength =
            Random.nextFloat() * (initialLengthRange.second - initialLengthRange.first) +
                    initialLengthRange.first
        val lengthReduction =
            Random.nextFloat() * (lengthReductionRange.second - lengthReductionRange.first) +
                    lengthReductionRange.first

        val params = LSystemParams(
            angle = angle,
            iterations = iterations,
            initialLength = initialLength,
            lengthReduction = lengthReduction
        )

        val commands = generateWithParams(formula, params)
        return commands to params
    }

    private fun generateWithParams(formula: LSystemFormula, params: LSystemParams): List<Command> {
        var current = formula.axiom
        repeat(params.iterations) {
            current = current.map { formula.rules[it] ?: it.toString() }.joinToString("")
        }
        return parseCommands(
            current,
            params.angle,
            params.initialLength,
            params.lengthReduction
        )
    }

    private fun parseCommands(
        axiom: String,
        angle: Float,
        initialLength: Float,
        lengthReduction: Float
    ): List<Command> {
        val commands = mutableListOf<Command>()
        var length = initialLength
        for (char in axiom) {
            when (char) {
                'F' -> commands.add(Command(Command.Type.FORWARD, length))
                'X' -> Unit
                '+' -> commands.add(Command(Command.Type.TURN_LEFT, angle = angle))
                '-' -> commands.add(Command(Command.Type.TURN_RIGHT, angle = angle))
                '[' -> {
                    commands.add(Command(Command.Type.PUSH))
                    length *= lengthReduction
                }
                ']' -> {
                    commands.add(Command(Command.Type.POP))
                    length /= lengthReduction
                }
            }
        }
        return commands
    }

    companion object {
        val defaultFormulas = listOf(
            LSystemFormula(
                axiom = "X",
                rules = mapOf('X' to "F[+X]F[-X]+X", 'F' to "FF"),
                defaultAngle = 25.7f
            ),
            LSystemFormula(
                axiom = "F",
                rules = mapOf('F' to "FF+[+F-F-F]-[-F+F+F]"),
                defaultAngle = 22.5f
            ),
            LSystemFormula(
                axiom = "F",
                rules = mapOf('F' to "F[+F]F[-F][F]"),
                defaultAngle = 25f
            ),
            LSystemFormula(
                axiom = "F",
                rules = mapOf('F' to "F[+F]F[-F]F"),
                defaultAngle = 25f
            )
        )
    }
}
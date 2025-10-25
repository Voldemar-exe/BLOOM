package com.example.habit.models

data class Command(val type: Type, val length: Float = 0f, val angle: Float = 0f) {
    enum class Type {
        FORWARD, TURN_LEFT, TURN_RIGHT, PUSH, POP
    }
}
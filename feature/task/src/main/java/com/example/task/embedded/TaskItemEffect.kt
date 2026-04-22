package com.example.task.embedded

sealed interface TaskItemEffect {
    object SaveSuccess : TaskItemEffect
}
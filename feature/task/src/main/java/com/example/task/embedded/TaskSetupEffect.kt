package com.example.task.embedded

sealed interface TaskSetupEffect {
    object SaveSuccess : TaskSetupEffect
}

package com.example.model

data class HabitWithRelations(
    val habit: Habit,
    val plant: HabitPlant,
    val reminders: List<Reminder>,
)

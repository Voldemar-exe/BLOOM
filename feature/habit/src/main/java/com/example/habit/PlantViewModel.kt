package com.example.habit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.habit.models.PlantState

class PlantViewModel(
//    private val renderer: PlantRenderer
) : ViewModel() {
    private val _plants = mutableStateOf<List<PlantState>>(emptyList())
    val plants = _plants

    init {

    }
}
package com.example.model

import androidx.compose.ui.graphics.Color

data class User(
    val userId: Long,
    val email: String,
    val username: String,
    val avatar: Int,
    val background: Int,
    val color: Color,
    val achievements: Set<Int>,
    val purchases: List<Pair<String, String>>
)

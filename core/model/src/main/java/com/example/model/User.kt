package com.example.model

data class User(
    val userId: Long,
    val email: String,
    val username: String,
    val avatar: Int,
    val background: Int,
    val color: Int,
    val achievements: Set<Int>,
    val purchases: List<Pair<String, String>>
)

package com.example.model

data class User(
    val userId: Long,
    val email: String,
    val username: String,
    val avatar: String,
    val background: String,
    val color: String,
    val achievements: Set<Int>,
    val purchases: List<Pair<String, String>>
)

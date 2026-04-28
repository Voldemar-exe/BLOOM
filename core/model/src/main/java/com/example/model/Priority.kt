package com.example.model

enum class Priority(override val ru: String) : LocalizedEnum {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий"),
    PRIMARY("Первичный"),
}

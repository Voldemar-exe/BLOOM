package com.example.model

enum class Tag(override val ru: String) : LocalizedEnum {
    WORK("работа"),
    STUDY("учёба"),
    FINANCE("финансы"),
    HOME("дом"),
    PAPERS("бумага"),
    SHOPPING("покупка"),
    MEETINGS("встреча"),
    TALKS("общение"),
    TECH("технология"),
    HEALTH("здоровье"),
    MAIN("главное"),
    PLANNING("планирование"),
    SPORT("спорт"),
    UPDATE("обновление"),
    TRAVEL("путешествие"),
    FAMILY("семья"),
    HELP("помощь"),
    HOBBIES("хобби"),
    SELF_DEVELOPMENT("саморазвитие"),
}

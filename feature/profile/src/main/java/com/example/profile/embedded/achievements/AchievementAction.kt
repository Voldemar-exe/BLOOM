package com.example.profile.embedded.achievements

import com.example.model.SortType

sealed interface AchievementAction {
    data class OnSortChange(val sortType: SortType) : AchievementAction
}

package com.example.habit.presentation.statistics

import com.example.habit.domain.model.HabitCategory

sealed class StatisticsScreenEvent {
    object NavigateToDayScreen : StatisticsScreenEvent()
    object NavigateToHabitsScreen : StatisticsScreenEvent()
    object DismissCategoryDropdown : StatisticsScreenEvent()
    object DismissPeriodDropdown : StatisticsScreenEvent()
    object OpenCategoryDropdown : StatisticsScreenEvent()
    object OpenPeriodDropdown : StatisticsScreenEvent()
    data class SetPeriodInDays(val days: Int) : StatisticsScreenEvent()
    data class SetCategory(val category: HabitCategory) : StatisticsScreenEvent()
}
package com.example.habit.presentation.statistics

sealed class StatisticsScreenEvent {
    object NavigateToDayScreen : StatisticsScreenEvent()
    object NavigateToHabitsScreen : StatisticsScreenEvent()
}
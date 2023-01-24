package com.example.habit.presentation.day

sealed class DayScreenEvent {
    data class ChangeHabitHistoryItemState(val id: Long) : DayScreenEvent()
    object NavigateToHabitsScreen : DayScreenEvent()
    object NavigateToStatistics : DayScreenEvent()
    object MinusOneDay : DayScreenEvent()
    object PlusOneDay : DayScreenEvent()
}
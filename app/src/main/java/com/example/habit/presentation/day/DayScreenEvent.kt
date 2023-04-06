package com.example.habit.presentation.day

import kotlinx.datetime.LocalTime

sealed class DayScreenEvent {
    data class ChangeHabitHistoryItemState(val id: Long) : DayScreenEvent()
    object NavigateToHabitsScreen : DayScreenEvent()
    object NavigateToStatistics : DayScreenEvent()
    object MinusOneDay : DayScreenEvent()
    object PlusOneDay : DayScreenEvent()
    object CloseItemEditor : DayScreenEvent()
    data class OpenItemEditor(val itemId: Long) : DayScreenEvent()
    data class EditSelectedItemTime(val time: LocalTime) : DayScreenEvent()
    object ChangeSelectedHabitHistoryItemState: DayScreenEvent()
}
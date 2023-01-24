package com.example.habit.presentation.habits

import kotlinx.datetime.LocalTime

sealed class HabitsScreenEvent {
    object OpenNewHabitCreator : HabitsScreenEvent()
    object CloseNewHabitCreator : HabitsScreenEvent()
    object CreateNewHabit : HabitsScreenEvent()
    data class EditNewHabitName(val name: String) : HabitsScreenEvent()
    data class EditNewHabitCategory(val category: String) : HabitsScreenEvent()
    data class EditNewHabitRepetition(val repetition: String) : HabitsScreenEvent()
    data class EditNewHabitTime(val time: LocalTime): HabitsScreenEvent()
    object NavigateToDayScreen : HabitsScreenEvent()
    data class DeleteHabit(val habitId: Long) : HabitsScreenEvent()
}
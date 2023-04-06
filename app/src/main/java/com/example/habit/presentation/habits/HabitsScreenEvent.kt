package com.example.habit.presentation.habits

import com.example.habit.domain.model.Habit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class HabitsScreenEvent {
    object OpenNewHabitCreator : HabitsScreenEvent()
    object CloseNewHabitCreator : HabitsScreenEvent()
    object CreateNewHabit : HabitsScreenEvent()
    data class EditNewHabitName(val name: String) : HabitsScreenEvent()
    data class EditNewHabitCategory(val category: String) : HabitsScreenEvent()
    data class EditNewHabitRepetition(val repetition: String) : HabitsScreenEvent()
    data class EditNewHabitTime(val time: LocalTime): HabitsScreenEvent()
    data class EditNewHabitDate(val date: LocalDate): HabitsScreenEvent()
    object NavigateToDayScreen : HabitsScreenEvent()
    object NavigateToStatistics : HabitsScreenEvent()
    data class DeleteHabit(val habitId: Long) : HabitsScreenEvent()
    data class OpenHabitEditor(val habit: Habit) : HabitsScreenEvent()
    object CloseHabitEditor : HabitsScreenEvent()
    object SaveEditedHabit : HabitsScreenEvent()
    data class EditEditedHabitName(val name: String) : HabitsScreenEvent()
    data class EditEditedHabitCategory(val category: String) : HabitsScreenEvent()
    data class EditEditedHabitTime(val time: LocalTime) : HabitsScreenEvent()
}
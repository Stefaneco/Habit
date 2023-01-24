package com.example.habit.presentation.habits

import com.example.habit.domain.Repetition
import com.example.habit.domain.model.Habit
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class HabitsScreenState(
    val isLoading: Boolean = true,
    val habits : List<Habit> = emptyList(),
    val isNewHabitCreatorOpen: Boolean = false,
    val isValidNewHabit: Boolean = false,
    val newHabitName: String = "",
    val newHabitCategory: String = "",
    val newHabitTime: LocalTime = LocalTime(10,0,0),
    val newHabitDate: LocalDateTime = DateTimeUtil.now(),
    val newHabitRepetition : String = Repetition.DAILY
)
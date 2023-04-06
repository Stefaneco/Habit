package com.example.habit.domain.interactors

import android.util.Log
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus

class UpdateHabitsNextOccurrences(
    val habitRepository: IHabitRepository,
    val upsertHabitHistoryItems: UpsertHabitHistoryItems
) {
    suspend operator fun invoke(
        habits: List<Habit>,
        minNextOccurrenceDateTime: LocalDateTime = DateTimeUtil.dayEnd(DateTimeUtil.nowPlus(days = 1))
    ) {
        val newHabitHistoryItems = mutableListOf<HabitHistoryItem>()
        for(habit in habits){
            while (minNextOccurrenceDateTime >= habit.nextOccurrence){
                Log.i("UpdateHabitsNextOccurrences", "minNext: $minNextOccurrenceDateTime nextOcc: ${habit.nextOccurrence}")
                val newHabitHistoryItem = HabitHistoryItem(
                    habitId = habit.id,
                    isDone = false,
                    dateTime = habit.nextOccurrence
                )
                habit.nextOccurrence =
                    habit.nextOccurrence.date.plus(DatePeriod(days = 1))
                    .atTime(habit.nextOccurrence.time)
                newHabitHistoryItems.add(newHabitHistoryItem)
            }
        }
        upsertHabitHistoryItems(newHabitHistoryItems)
        habitRepository.updateHabits(habits)
    }
}
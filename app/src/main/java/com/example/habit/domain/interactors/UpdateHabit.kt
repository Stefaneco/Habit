package com.example.habit.domain.interactors

import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.repositories.ICategoryRepository
import com.example.habit.domain.repositories.IHabitHistoryRepository
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

/**
Updates provided Habit.

Updates HabitHistoryItems and notifications of provided Habit.
 */
class UpdateHabit(
    private val habitRepository: IHabitRepository,
    private val habitHistoryRepository: IHabitHistoryRepository,
    private val categoryRepository: ICategoryRepository,
    private val upsertHabitHistoryItems: UpsertHabitHistoryItems
) {

    suspend operator fun invoke(habit: Habit, currentDateTime: LocalDateTime = DateTimeUtil.now()){
        updateFutureHabitHistoryItemsOfHabit(habit, currentDateTime)
        if(habit.start.time != habit.nextOccurrence.time){
            val newNextOccurrenceDateTime =
                habit.nextOccurrence.date
                    .atTime(habit.start.time)
            habit.nextOccurrence = newNextOccurrenceDateTime
        }

        habit.category.id = getCategoryIdByNameOrInsertCategory(habit.category)
        habitRepository.updateHabit(habit)
    }

    private suspend fun updateFutureHabitHistoryItemsOfHabit(habit: Habit, currentDateTime: LocalDateTime){
        val futureHistoryItems = habitHistoryRepository.getHabitHistoryItemsByHabitId(
            fromTimestamp = DateTimeUtil.toEpochMillis(currentDateTime),
            habitId = habit.id
        )
        val newFutureHistoryItems = futureHistoryItems.map {
            val newDateTime = it.dateTime.date
                .atTime(habit.start.time)
            it.copy(
                dateTime = newDateTime,
                habitName = habit.name
            )
        }
        upsertHabitHistoryItems(newFutureHistoryItems, currentDateTime)
    }

    private suspend fun getCategoryIdByNameOrInsertCategory(category : HabitCategory) : Long{
        val isCategoryInDb = categoryRepository.isCategoryNameInDb(category.name)
        return if(!isCategoryInDb) {
            categoryRepository.insertCategory(category)
        } else {
            categoryRepository.getCategoryByName(category.name).id
        }
    }
}
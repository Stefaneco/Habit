package com.example.habit.domain.interactors

import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.repositories.ICategoryRepository
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.domain.util.rangeTo
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus

/**
Inserts new habit into the database.

Inserts new habit category into the database if category with given name doesn't exist.

Inserts new habit history items from habits start to current day + 1.

Schedules notifications for future habit history items.
 */
class CreateHabit(
    private val habitRepository: IHabitRepository,
    private val categoryRepository: ICategoryRepository,
    private val upsertHabitHistoryItems: UpsertHabitHistoryItems
) {
    suspend operator fun invoke(habit: Habit, currentDateTime: LocalDateTime = DateTimeUtil.now()) : Long {

        habit.category.id = getCategoryIdByNameOrInsertCategory(habit.category)
        val newHabitId = habitRepository.insertHabit(habit)
        createHabitHistoryItemsForHabit(habit.copy(id = newHabitId), currentDateTime)
        return newHabitId
    }

    private suspend fun createHabitHistoryItemsForHabit(habit: Habit, currentDateTime: LocalDateTime) {
        val newHabitDateTime = DateTimeUtil.fromEpochMillis(habit.start)
        val newHabitDate = newHabitDateTime.date
        val newHabitTime = newHabitDateTime.time

        val newHabitHistoryItems = mutableListOf<HabitHistoryItem>()
        for(date in newHabitDate..currentDateTime.date.plus(DatePeriod(days = 1))){
            newHabitHistoryItems.add(
                HabitHistoryItem(
                    habitId = habit.id,
                    habitName = habit.name,
                    dateTimeTimestamp = DateTimeUtil.toEpochMillis(date.atTime(newHabitTime))
                )
            )
        }
        upsertHabitHistoryItems(newHabitHistoryItems, currentDateTime)
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
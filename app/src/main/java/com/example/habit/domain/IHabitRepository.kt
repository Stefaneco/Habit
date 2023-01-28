package com.example.habit.domain

import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.model.dto.HabitName

interface IHabitRepository {

    suspend fun getAllHabits() : List<Habit>
    suspend fun insertHabit(habit: Habit) : Long
    suspend fun getHabitHistoryItems(fromTimestamp: Long, toTimestamp: Long) : List<HabitHistoryItem>
    suspend fun getHabitHistoryItemsByCategoryId(fromTimestamp: Long, toTimestamp: Long, categoryId : Long) : List<HabitHistoryItem>
    suspend fun getHabitHistoryItemsByHabitId(fromTimestamp: Long, toTimestamp: Long, habitId: Long) : List<HabitHistoryItem>
    suspend fun insertHabitHistoryItems(habitItems: List<HabitHistoryItem>)
    suspend fun updateHabits(habits: List<Habit>)
    suspend fun upsertHabitHistoryItem(habitHistoryItem: HabitHistoryItem)
    suspend fun deleteHabit(habitId: Long, deletePlanned: Boolean = true, deleteHistory: Boolean = false)
    suspend fun getAllCategories() : List<HabitCategory>
    suspend fun getHabitsNamesByCategory(categoryId: Long) : List<HabitName>
    suspend fun getHabit(habitId: Long) : Habit
}
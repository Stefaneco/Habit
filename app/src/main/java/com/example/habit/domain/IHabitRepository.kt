package com.example.habit.domain

import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitHistoryItem

interface IHabitRepository {

    suspend fun getAllHabits() : List<Habit>
    suspend fun insertHabit(habit: Habit) : Long
    suspend fun getHabitHistoryItems(fromTimestamp: Long, toTimestamp: Long) : List<HabitHistoryItem>
    suspend fun getHabitHistoryItems(fromTimestamp: Long, toTimestamp: Long, categoryId : Long) : List<HabitHistoryItem>
    suspend fun insertHabitHistoryItems(habitItems: List<HabitHistoryItem>)
    suspend fun updateHabits(habits: List<Habit>)
    suspend fun upsertHabitHistoryItem(habitHistoryItem: HabitHistoryItem)
    suspend fun deleteHabit(habitId: Long, deletePlanned: Boolean = true, deleteHistory: Boolean = false)
}
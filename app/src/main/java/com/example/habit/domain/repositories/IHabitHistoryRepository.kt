package com.example.habit.domain.repositories

import com.example.habit.domain.model.HabitHistoryItem
import kotlinx.datetime.LocalDate

interface IHabitHistoryRepository {
    suspend fun upsertAllHabitHistoryItems(habitItems: List<HabitHistoryItem>) : List<Long>
    suspend fun upsertHabitHistoryItem(habitItem : HabitHistoryItem) : Long
    suspend fun getHabitHistoryItems(fromTimestamp: Long, toTimestamp: Long) : List<HabitHistoryItem>
    suspend fun getHabitHistoryItemsByCategoryId(fromTimestamp: Long, toTimestamp: Long, categoryId : Long) : List<HabitHistoryItem>
    suspend fun getHabitHistoryItemsByHabitId(fromTimestamp: Long, toTimestamp: Long, habitId: Long) : List<HabitHistoryItem>
    suspend fun getHabitHistoryItemsByHabitId(fromTimestamp: Long, habitId: Long) : List<HabitHistoryItem>
    suspend fun getEarliestHabitHistoryItemDate(): LocalDate
    suspend fun getLatestHabitHistoryItemDate(): LocalDate
}
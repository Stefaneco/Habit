package com.example.habit.domain

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil

class HabitRepository(db: AppDatabase) : IHabitRepository {
    private val habitDao = db.habitDao()
    private val habitHistoryDao = db.habitHistoryDao()

    override suspend fun getAllHabits() : List<Habit> {
        return habitDao.getAllHabits().map { Habit.fromHabitEntity(it) }
    }

    override suspend fun insertHabit(habit: Habit) : Long{
        return habitDao.insertHabit(Habit.toHabitEntity(habit))
    }

    override suspend fun upsertHabitHistoryItem(habitHistoryItem: HabitHistoryItem){
        return habitHistoryDao.upsertHabitHistoryItem(HabitHistoryItem.toHabitHistoryItemEntity(habitHistoryItem))
    }

    override suspend fun deleteHabit(
        habitId: Long,
        deletePlanned: Boolean,
        deleteHistory: Boolean
    ) {
        habitDao.deleteHabitById(habitId)
        if(deleteHistory) habitHistoryDao.deleteHistoryItemsByHabitId(habitId)
        else if(deletePlanned) habitHistoryDao.deletePlannedHistoryItemsByHabitId(habitId, DateTimeUtil.nowEpochMillis())
    }

    override suspend fun getHabitHistoryItems(
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtos(fromTimestamp,toTimestamp)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun insertHabitHistoryItems(habitItems: List<HabitHistoryItem>) {
        habitHistoryDao.insertAllHabitHistoryItems(habitItems.map { HabitHistoryItem.toHabitHistoryItemEntity(it) })
    }

    override suspend fun updateHabits(habits: List<Habit>) {
        habitDao.insertHabits(habits.map { Habit.toHabitEntity(it) })
    }
}
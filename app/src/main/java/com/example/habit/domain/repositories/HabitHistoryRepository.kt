package com.example.habit.domain.repositories

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDate

class HabitHistoryRepository(
    db: AppDatabase,
) : IHabitHistoryRepository {

    private val habitHistoryDao = db.habitHistoryDao()

    override suspend fun upsertAllHabitHistoryItems(habitItems: List<HabitHistoryItem>): List<Long> {
        return habitHistoryDao.upsertAllHabitHistoryItems(habitItems.map { HabitHistoryItem.toHabitHistoryItemEntity(it) })
    }

    override suspend fun upsertHabitHistoryItem(habitItem: HabitHistoryItem): Long {
        return habitHistoryDao.upsertHabitHistoryItem(HabitHistoryItem.toHabitHistoryItemEntity(habitItem))
    }

    override suspend fun getHabitHistoryItems(
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtos(fromTimestamp,toTimestamp)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun getHabitHistoryItemsByCategoryId(fromTimestamp: Long, toTimestamp: Long, categoryId: Long): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtosByCategoryId(fromTimestamp,toTimestamp,categoryId)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun getHabitHistoryItemsByHabitId(fromTimestamp: Long, toTimestamp: Long, habitId: Long): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtosByHabitId(fromTimestamp,toTimestamp,habitId)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun getHabitHistoryItemsByHabitId(
        fromTimestamp: Long,
        habitId: Long
    ): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtosByHabitId(fromTimestamp,habitId)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun getEarliestHabitHistoryItemDate(): LocalDate {
        val minTimestamp = habitHistoryDao.getMinHabitHistoryItemTimestamp()
        return DateTimeUtil.fromEpochMillis(minTimestamp).date
    }

    override suspend fun getLatestHabitHistoryItemDate(): LocalDate {
        val maxTimestamp = habitHistoryDao.getMaxHabitHistoryItemTimestamp()
        return DateTimeUtil.fromEpochMillis(maxTimestamp).date
    }
}
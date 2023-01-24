package com.example.habit.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habit.data.room.dto.HabitHistoryItemDto
import com.example.habit.data.room.entities.HabitHistoryItemEntity

@Dao
interface HabitHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabitHistoryItem(habitItem: HabitHistoryItemEntity)

    @Insert
    suspend fun insertAllHabitHistoryItems(habitItems: List<HabitHistoryItemEntity>)


    @Query("SELECT * FROM habitHistory WHERE dateTimeTimestamp BETWEEN :from AND :to")
    suspend fun getHabitHistoryItems(from: Long, to: Long) : List<HabitHistoryItemEntity>

    @Query("SELECT habitHistory.id AS id, " +
            "habitHistory.habitId as habitId, " +
            "habitHistory.isDone AS isDone," +
            "habitHistory.dateTimeTimestamp AS dateTimeTimestamp," +
            "habits.name AS habitName " +
            "FROM habitHistory, habits " +
            "WHERE habitHistory.habitId = habits.Id " +
            "AND habitHistory.dateTimeTimestamp BETWEEN :from AND :to")
    suspend fun getHabitHistoryItemDtos(from: Long, to: Long) : List<HabitHistoryItemDto>

    @Query("DELETE FROM habitHistory WHERE habitId=:habitId")
    suspend fun deleteHistoryItemsByHabitId(habitId: Long)

    @Query("DELETE FROM habitHistory WHERE habitId=:habitId AND dateTimeTimestamp>:deleteAfterTimestamp")
    suspend fun deletePlannedHistoryItemsByHabitId(habitId: Long, deleteAfterTimestamp: Long)
}
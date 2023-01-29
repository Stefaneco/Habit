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
            "habitHistory.doneTimestamp AS doneTimestamp," +
            "habits.name AS habitName " +
            "FROM habitHistory " +
            "INNER JOIN habits ON habits.id = habitHistory.habitId " +
            "WHERE habitHistory.habitId = habits.Id " +
            "AND habitHistory.dateTimeTimestamp BETWEEN :from AND :to")
    suspend fun getHabitHistoryItemDtos(from: Long, to: Long) : List<HabitHistoryItemDto>

    @Query("SELECT habitHistory.id AS id, " +
            "habitHistory.habitId as habitId, " +
            "habitHistory.isDone AS isDone," +
            "habitHistory.dateTimeTimestamp AS dateTimeTimestamp," +
            "habitHistory.doneTimestamp AS doneTimestamp," +
            "habits.name AS habitName " +
            "FROM habitHistory " +
            "INNER JOIN habits ON habits.id = habitHistory.habitId " +
            "WHERE habitHistory.habitId = habits.Id " +
            "AND habits.categoryId = :categoryId " +
            "AND habitHistory.dateTimeTimestamp BETWEEN :from AND :to")
    suspend fun getHabitHistoryItemDtosByCategoryId(from: Long, to: Long, categoryId: Long) : List<HabitHistoryItemDto>

    @Query("SELECT habitHistory.id AS id, " +
            "habitHistory.habitId as habitId, " +
            "habitHistory.isDone AS isDone," +
            "habitHistory.dateTimeTimestamp AS dateTimeTimestamp," +
            "habitHistory.doneTimestamp AS doneTimestamp," +
            "habits.name AS habitName " +
            "FROM habitHistory " +
            "INNER JOIN habits ON habits.id = habitHistory.habitId " +
            "WHERE habitHistory.habitId = habits.Id " +
            "AND habitHistory.habitId = :habitId " +
            "AND habitHistory.dateTimeTimestamp BETWEEN :from AND :to")
    suspend fun getHabitHistoryItemDtosByHabitId(from: Long, to: Long, habitId: Long) : List<HabitHistoryItemDto>

    @Query("SELECT habitHistory.id AS id, " +
            "habitHistory.habitId as habitId, " +
            "habitHistory.isDone AS isDone," +
            "habitHistory.dateTimeTimestamp AS dateTimeTimestamp," +
            "habitHistory.doneTimestamp AS doneTimestamp," +
            "habits.name AS habitName " +
            "FROM habitHistory " +
            "INNER JOIN habits ON habits.id = habitHistory.habitId " +
            "WHERE habitHistory.habitId = habits.Id " +
            "AND habitHistory.habitId = :habitId ")
    suspend fun getHabitHistoryItemDtosByHabitId(habitId: Long) : List<HabitHistoryItemDto>

    @Query("DELETE FROM habitHistory WHERE habitId=:habitId")
    suspend fun deleteHistoryItemsByHabitId(habitId: Long)

    @Query("DELETE FROM habitHistory WHERE ((habitId=:habitId) AND (dateTimeTimestamp > :deleteAfterTimestamp))")
    suspend fun deletePlannedHistoryItemsByHabitId(habitId: Long, deleteAfterTimestamp: Long)
}
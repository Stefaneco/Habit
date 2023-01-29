package com.example.habit.data.room.dao

import androidx.room.*
import com.example.habit.data.room.dto.HabitDto
import com.example.habit.data.room.dto.HabitNameDto
import com.example.habit.data.room.entities.HabitEntity

@Dao
interface HabitDao {

    @Query("Select * FROM habits")
    suspend fun getAllHabits() : List<HabitEntity>

    @Query("Select * FROM habits WHERE id=:id")
    suspend fun getHabit(id: Long) : HabitEntity

    @Query("Select " +
            "habits.id," +
            "habits.name," +
            "habits.start," +
            "habits.nextOccurrence," +
            "habits.repetition," +
            "habits.categoryId," +
            "habits.isDeleted," +
            "categories.name AS categoryName " +
            "FROM habits INNER JOIN categories ON habits.categoryId = categories.id " +
            "WHERE habits.id=:id AND habits.isDeleted=:isDeleted")
    suspend fun getHabitDto(id: Long, isDeleted: Boolean) : HabitDto

    @Query("Select id, name FROM habits WHERE categoryId=:categoryId")
    suspend fun getHabitNamesByCategoryId(categoryId: Long): List<HabitNameDto>

    @Query("Select " +
            "habits.id," +
            "habits.name," +
            "habits.start," +
            "habits.nextOccurrence," +
            "habits.repetition," +
            "habits.categoryId," +
            "habits.isDeleted," +
            "categories.name AS categoryName " +
            "FROM habits INNER JOIN categories ON habits.categoryId = categories.id " +
            "WHERE habits.isDeleted=:isDeleted")
    suspend fun getAllHabitsDto(isDeleted: Boolean): List<HabitDto>

    @Insert
    suspend fun insertHabit(habitEntity: HabitEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habitEntity: List<HabitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabit(habitEntity: HabitEntity) : Long

    @Query("DELETE FROM habits WHERE id=:id")
    suspend fun deleteHabitById(id: Long)
}
package com.example.habit.data.room.dao

import androidx.room.*
import com.example.habit.data.room.dto.HabitDto
import com.example.habit.data.room.dto.HabitNameDto
import com.example.habit.data.room.entities.HabitEntity

@Dao
interface HabitDao {

    @Query("Select * FROM habits")
    suspend fun getAllHabits() : List<HabitEntity>

    @Query("Select " +
            "habits.id," +
            "habits.name," +
            "habits.start," +
            "habits.nextOccurrence," +
            "habits.repetition," +
            "habits.categoryId," +
            "categories.name AS categoryName " +
            "FROM habits INNER JOIN categories ON habits.categoryId = categories.id " +
            "WHERE habits.id=:id")
    suspend fun getHabitDto(id: Long) : HabitDto

    @Query("Select id, name FROM habits WHERE categoryId=:categoryId")
    suspend fun getHabitNamesByCategoryId(categoryId: Long): List<HabitNameDto>

    @Query("Select " +
            "habits.id," +
            "habits.name," +
            "habits.start," +
            "habits.nextOccurrence," +
            "habits.repetition," +
            "habits.categoryId," +
            "categories.name AS categoryName " +
            "FROM habits INNER JOIN categories ON habits.categoryId = categories.id")
    suspend fun getAllHabitsDto(): List<HabitDto>

    @Insert
    suspend fun insertHabit(habitEntity: HabitEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habitEntity: List<HabitEntity>)

    @Query("DELETE FROM habits WHERE id=:id")
    suspend fun deleteHabitById(id: Long)
}
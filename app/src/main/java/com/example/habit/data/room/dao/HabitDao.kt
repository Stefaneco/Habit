package com.example.habit.data.room.dao

import androidx.room.*
import com.example.habit.data.room.entities.HabitEntity

@Dao
interface HabitDao {

    @Query("Select * FROM habits")
    suspend fun getAllHabits() : List<HabitEntity>

    @Insert
    suspend fun insertHabit(habitEntity: HabitEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habitEntity: List<HabitEntity>)

    @Query("DELETE FROM habits WHERE id=:id")
    suspend fun deleteHabitById(id: Long)
}
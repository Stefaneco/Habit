package com.example.habit.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habit.data.room.entities.HabitCategoryEntity

@Dao
interface HabitCategoryDto {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHabitCategory(habitCategoryEntity: HabitCategoryEntity) : Long

    @Query("SELECT EXISTS(SELECT * FROM categories WHERE name=:name)")
    suspend fun isCategoryNameInDb(name: String) : Boolean

    @Query("SELECT * FROM categories WHERE name=:name")
    suspend fun getHabitCategoryByName(name: String) : HabitCategoryEntity
}
package com.example.habit.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habit.data.room.dao.HabitCategoryDto
import com.example.habit.data.room.dao.HabitDao
import com.example.habit.data.room.dao.HabitHistoryDao
import com.example.habit.data.room.entities.HabitCategoryEntity
import com.example.habit.data.room.entities.HabitEntity
import com.example.habit.data.room.entities.HabitHistoryItemEntity

@Database(entities = [HabitEntity::class, HabitHistoryItemEntity::class, HabitCategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitHistoryDao(): HabitHistoryDao
    abstract fun habitCategoryDao(): HabitCategoryDto
}
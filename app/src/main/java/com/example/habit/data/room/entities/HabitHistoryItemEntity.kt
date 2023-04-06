package com.example.habit.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitHistory")
data class HabitHistoryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val isDone: Boolean,
    val doneTimestamp: Long?,
    val dateTimeTimestamp: Long
)
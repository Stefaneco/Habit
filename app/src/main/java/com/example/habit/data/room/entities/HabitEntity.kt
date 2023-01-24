package com.example.habit.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val start: Long,
    val nextOccurrence: Long,
    val repetition: String,
    val category: String
)

package com.example.habit.data.room.dto

data class HabitHistoryItemDto(
    val id: Long = 0,
    val habitName: String,
    val habitId: Long,
    val isDone: Boolean,
    val doneTimestamp: Long?,
    val dateTimeTimestamp: Long,
) {
}
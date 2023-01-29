package com.example.habit.data.room.dto

data class HabitDto(
    val id: Long = 0,
    val name: String,
    val start: Long,
    val nextOccurrence: Long,
    val repetition: String,
    val categoryId: Long,
    val categoryName: String,
    val isDeleted: Boolean
) {
}
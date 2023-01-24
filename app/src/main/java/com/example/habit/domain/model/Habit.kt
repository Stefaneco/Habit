package com.example.habit.domain.model

import com.example.habit.data.room.entities.HabitEntity

data class Habit(
    val id: Long = 0,
    val name: String,
    val start: Long,
    var nextOccurrence: Long,
    val repetition: String,
    val category: String
) {
    companion object {
        fun fromHabitEntity(habitEntity: HabitEntity): Habit {
            return Habit(
                id = habitEntity.id,
                name = habitEntity.name,
                start = habitEntity.start,
                nextOccurrence = habitEntity.nextOccurrence,
                repetition = habitEntity.repetition,
                category = habitEntity.category
            )
        }

        fun toHabitEntity(habit: Habit) : HabitEntity
        {
            return HabitEntity(
                id = habit.id,
                name = habit.name,
                start = habit.start,
                nextOccurrence = habit.nextOccurrence,
                repetition = habit.repetition,
                category = habit.category
            )
        }
    }
}
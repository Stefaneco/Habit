package com.example.habit.domain.model

import com.example.habit.data.room.dto.HabitDto
import com.example.habit.data.room.entities.HabitEntity
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime

data class Habit(
    val id: Long = 0,
    val name: String,
    val start: LocalDateTime,
    var nextOccurrence: LocalDateTime = LocalDateTime(0,0,0,0,0),
    val repetition: String,
    val category: HabitCategory,
    val isDeleted: Boolean = false
) {
    companion object {
        /*fun fromHabitEntity(habitEntity: HabitEntity): Habit {
            return Habit(
                id = habitEntity.id,
                name = habitEntity.name,
                start = habitEntity.start,
                nextOccurrence = habitEntity.nextOccurrence,
                repetition = habitEntity.repetition
            )
        }*/

        fun fromHabitDto(habitDto: HabitDto) : Habit {
            return Habit(
                id = habitDto.id,
                name = habitDto.name,
                start = DateTimeUtil.fromEpochMillis(habitDto.start),
                nextOccurrence = DateTimeUtil.fromEpochMillis(habitDto.nextOccurrence),
                repetition = habitDto.repetition,
                category = HabitCategory(habitDto.categoryId, habitDto.categoryName),
                isDeleted = habitDto.isDeleted
            )
        }

        fun toHabitEntity(habit: Habit) : HabitEntity
        {
            return HabitEntity(
                id = habit.id,
                name = habit.name,
                start = DateTimeUtil.toEpochMillis(habit.start),
                nextOccurrence = DateTimeUtil.toEpochMillis(habit.nextOccurrence),
                repetition = habit.repetition,
                categoryId = habit.category.id
            )
        }
    }
}
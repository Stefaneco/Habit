package com.example.habit.domain.model.dto

import com.example.habit.data.room.dto.HabitNameDto

data class HabitName(
    val id: Long,
    val name: String
) {
    companion object {
        fun fromHabitNameDto(habitNameDto: HabitNameDto) : HabitName{
            return HabitName(
                id = habitNameDto.id,
                name = habitNameDto.name
            )
        }
    }
}

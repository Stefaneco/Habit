package com.example.habit.domain.model

import com.example.habit.data.room.dto.HabitHistoryItemDto
import com.example.habit.data.room.entities.HabitHistoryItemEntity

data class HabitHistoryItem(
    val id: Long = 0,
    val habitName: String = "",
    val habitId: Long,
    var isDone: Boolean = false,
    var doneTimestamp: Long? = null,
    val dateTimeTimestamp: Long
) {
    companion object {
        fun fromHabitHistoryItemDto(habitHistoryItemDto: HabitHistoryItemDto): HabitHistoryItem {
            return HabitHistoryItem(
                id = habitHistoryItemDto.id,
                habitName = habitHistoryItemDto.habitName,
                habitId = habitHistoryItemDto.habitId,
                isDone = habitHistoryItemDto.isDone,
                doneTimestamp = habitHistoryItemDto.doneTimestamp,
                dateTimeTimestamp = habitHistoryItemDto.dateTimeTimestamp
            )
        }

        fun toHabitHistoryItemEntity(habitHistoryItem: HabitHistoryItem) : HabitHistoryItemEntity {
            return HabitHistoryItemEntity(
                id = habitHistoryItem.id,
                habitId = habitHistoryItem.habitId,
                isDone = habitHistoryItem.isDone,
                doneTimestamp = habitHistoryItem.doneTimestamp,
                dateTimeTimestamp = habitHistoryItem.dateTimeTimestamp
            )
        }
    }
}
package com.example.habit.domain.model

import com.example.habit.data.room.dto.HabitHistoryItemDto
import com.example.habit.data.room.entities.HabitHistoryItemEntity
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime

data class HabitHistoryItem(
    val id: Long = 0,
    val habitName: String = "",
    val habitId: Long,
    var isDone: Boolean = false,
    var doneDateTime: LocalDateTime? = null,
    val dateTime: LocalDateTime
) {
    companion object {
        fun fromHabitHistoryItemDto(habitHistoryItemDto: HabitHistoryItemDto): HabitHistoryItem {
            return HabitHistoryItem(
                id = habitHistoryItemDto.id,
                habitName = habitHistoryItemDto.habitName,
                habitId = habitHistoryItemDto.habitId,
                isDone = habitHistoryItemDto.isDone,
                doneDateTime = habitHistoryItemDto.doneTimestamp?.let { DateTimeUtil.fromEpochMillis(it) },
                dateTime = DateTimeUtil.fromEpochMillis(habitHistoryItemDto.dateTimeTimestamp)
            )
        }

        fun toHabitHistoryItemEntity(habitHistoryItem: HabitHistoryItem) : HabitHistoryItemEntity {
            return HabitHistoryItemEntity(
                id = habitHistoryItem.id,
                habitId = habitHistoryItem.habitId,
                isDone = habitHistoryItem.isDone,
                doneTimestamp = habitHistoryItem.doneDateTime?.let { DateTimeUtil.toEpochMillis(it) },
                dateTimeTimestamp = DateTimeUtil.toEpochMillis(habitHistoryItem.dateTime)
            )
        }
    }
}
package com.example.habit.domain.interactors

import android.util.Log
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.notification.alarm.IAlarmScheduler
import com.example.habit.domain.notification.model.HabitReminderInfo
import com.example.habit.domain.repositories.IHabitHistoryRepository
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime

/**
    Upsert HabitHistoryItems into database.

    Update notifications for provided HabitHistoryItems.
*/
class UpsertHabitHistoryItems(
    private val habitHistoryRepository: IHabitHistoryRepository,
    private val alarmScheduler: IAlarmScheduler
) {

    suspend operator fun invoke(habitItem: HabitHistoryItem, currentDateTime: LocalDateTime = DateTimeUtil.now()){
        val id = habitHistoryRepository.upsertHabitHistoryItem(habitItem)
        updateHabitHistoryItemNotification(habitItem.copy(id = id), currentDateTime)
    }

    suspend operator fun invoke(habitItems: List<HabitHistoryItem>, currentDateTime: LocalDateTime = DateTimeUtil.now()){
        val ids = habitHistoryRepository.upsertAllHabitHistoryItems(habitItems)
        for(i in ids.indices){
            updateHabitHistoryItemNotification(habitItems[i].copy(id = ids[i]), currentDateTime)
        }
    }

    private fun updateHabitHistoryItemNotification(habitHistoryItem: HabitHistoryItem, currentDateTime: LocalDateTime) {
        if(!habitHistoryItem.isDone && habitHistoryItem.dateTime > currentDateTime){
            Log.i("UpsertHabitHistoryItems", "Updating notification")
            alarmScheduler.schedule(
                HabitReminderInfo(
                    id = habitHistoryItem.id.toInt(),
                    habitName = habitHistoryItem.habitName,
                    time = DateTimeUtil.toEpochMillis(habitHistoryItem.dateTime)
                )
            )
        }
        else{
            Log.i("UpsertHabitHistoryItems", "Cancelling notification")
            alarmScheduler.cancel(habitHistoryItem.id.toInt())
        }
    }
}
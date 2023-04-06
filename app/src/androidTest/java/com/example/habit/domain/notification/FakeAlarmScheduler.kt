package com.example.habit.domain.notification

import com.example.habit.domain.notification.alarm.IAlarmScheduler
import com.example.habit.domain.notification.model.HabitReminderInfo
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime

class FakeAlarmScheduler : IAlarmScheduler {

    private val reminders = mutableListOf<HabitReminderInfo>()

    override fun schedule(reminderInfo: HabitReminderInfo) {
        reminders.removeIf { reminder ->
            reminder.id == reminderInfo.id
        }
        reminders.add(reminderInfo)
    }

    override fun cancel(id: Int) {
        reminders.removeIf { reminder ->
            reminder.id == id
        }
    }

    fun removeRemindersBeforeDateTime(datetime: LocalDateTime){
        val timestamp = DateTimeUtil.toEpochMillis(dateTime = datetime)
        reminders.removeIf { reminder ->
            reminder.time < timestamp
        }
    }

    fun getCurrentReminders() : List<HabitReminderInfo> = reminders
}
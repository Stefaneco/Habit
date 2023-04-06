package com.example.habit.domain.notification.alarm

import com.example.habit.domain.notification.model.HabitReminderInfo

interface IAlarmScheduler  {
    fun schedule(reminderInfo: HabitReminderInfo)
    fun cancel(id: Int)
}
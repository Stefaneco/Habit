package com.example.habit.domain.notification.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.habit.domain.notification.model.HabitReminderInfo

class AndroidAlarmScheduler(
    private val context: Context
): IAlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(reminderInfo: HabitReminderInfo) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("HABIT_NAME", reminderInfo.habitName)
            putExtra("HABIT_ITEM_ID", reminderInfo.id)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderInfo.time,
            PendingIntent.getBroadcast(
                context,
                reminderInfo.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(id: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
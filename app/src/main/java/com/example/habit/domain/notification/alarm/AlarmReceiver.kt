package com.example.habit.domain.notification.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.habit.MainActivity
import com.example.habit.R

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val habitName = intent?.getStringExtra("HABIT_NAME") ?: return
        val habitItemId = intent.getIntExtra("HABIT_ITEM_ID", 0)

        context?.let {
            sendHabitNotification(it, habitName, habitItemId)
        }
    }

    private fun sendHabitNotification(
        context: Context, habitName: String, itemId: Int
    ){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "habit_channel")
            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
            .setContentTitle(habitName)
            .setContentText("Hey! ${context.getString(R.string.its_time_for)} $habitName.")
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
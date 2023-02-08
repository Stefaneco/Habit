package com.example.habit.domain.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habit.domain.interactors.UpsertHabitHistoryItems
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val habitRepository: IHabitRepository,
    val upsertHabitHistoryItems: UpsertHabitHistoryItems
    ): CoroutineWorker(context,workerParams) {

    override suspend fun doWork(): Result {
        //Get all habits
        //if nextOccurrence <= currentDateTime + 1 days
        //nextOccurrence += 1 day
        //Create new habitHistoryItem

        val habits = habitRepository.getAllHabits()
        val minNextOccurrenceDateTime = DateTimeUtil.nowPlus(days = 1)
        val minNextOccurrenceTimestamp = DateTimeUtil.dayEndEpochMillis(minNextOccurrenceDateTime.date)
        val newHabitHistoryItems = mutableListOf<HabitHistoryItem>()
        for(habit in habits){
            if (minNextOccurrenceTimestamp >= habit.nextOccurrence){
                val newHabitHistoryItem = HabitHistoryItem(
                    habitId = habit.id,
                    isDone = false,
                    dateTimeTimestamp = habit.nextOccurrence
                )
                habit.nextOccurrence += DateTimeUtil.DAY_IN_MILLIS
                newHabitHistoryItems.add(newHabitHistoryItem)
            }
        }
        upsertHabitHistoryItems(newHabitHistoryItems)
        habitRepository.updateHabits(habits)

        return Result.success()
    }
}
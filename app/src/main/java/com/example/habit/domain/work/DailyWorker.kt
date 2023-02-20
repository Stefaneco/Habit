package com.example.habit.domain.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habit.domain.interactors.UpsertHabitHistoryItems
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.domain.util.plusAssign
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.DatePeriod

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
        val newHabitHistoryItems = mutableListOf<HabitHistoryItem>()
        for(habit in habits){
            while (minNextOccurrenceDateTime >= habit.nextOccurrence){
                val newHabitHistoryItem = HabitHistoryItem(
                    habitId = habit.id,
                    isDone = false,
                    dateTime = habit.nextOccurrence
                )
                //habit.nextOccurrence += DateTimeUtil.DAY_IN_MILLIS
                habit.nextOccurrence += DatePeriod(days = 1)
                newHabitHistoryItems.add(newHabitHistoryItem)
            }
        }
        upsertHabitHistoryItems(newHabitHistoryItems)
        habitRepository.updateHabits(habits)

        return Result.success()
    }

}
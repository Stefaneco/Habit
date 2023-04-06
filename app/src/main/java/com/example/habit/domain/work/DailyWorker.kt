package com.example.habit.domain.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habit.domain.interactors.UpdateHabitsNextOccurrences
import com.example.habit.domain.repositories.IHabitRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val habitRepository: IHabitRepository,
    val updateHabitsNextOccurrences: UpdateHabitsNextOccurrences
    ): CoroutineWorker(context,workerParams) {

    override suspend fun doWork(): Result {
        val habits = habitRepository.getAllHabits()
        updateHabitsNextOccurrences(habits)

        return Result.success()
    }
}
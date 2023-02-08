package com.example.habit.domain.repositories

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.dto.HabitName
import com.example.habit.domain.util.DateTimeUtil

class HabitRepository(
    db: AppDatabase
) : IHabitRepository {
    private val habitDao = db.habitDao()
    private val habitHistoryDao = db.habitHistoryDao()

    override suspend fun getAllHabits() : List<Habit> {
        return habitDao.getAllHabitsDto(false).map { Habit.fromHabitDto(it) }
    }

    override suspend fun insertHabit(habit: Habit) : Long{
        return habitDao.insertHabit(Habit.toHabitEntity(habit))
    }

    override suspend fun deleteHabit(
        habitId: Long,
        deletePlanned: Boolean,
        deleteHistory: Boolean
    ) {
        val habit = habitDao.getHabit(habitId)
        habitDao.upsertHabit(habit.copy(isDeleted = true))
        if(deleteHistory){
            habitHistoryDao.deleteHistoryItemsByHabitId(habitId)
        }
        else if(deletePlanned){
            habitHistoryDao.deletePlannedHistoryItemsByHabitId(habitId, DateTimeUtil.nowEpochMillis())
        }
    }

    override suspend fun getHabitsNamesByCategory(categoryId: Long): List<HabitName> {
        return habitDao.getHabitNamesByCategoryId(categoryId).map { HabitName.fromHabitNameDto(it) }
    }

    override suspend fun getHabit(habitId: Long): Habit {
        return Habit.fromHabitDto(habitDao.getHabitDto(habitId, false))
    }

    override suspend fun updateHabits(habits: List<Habit>) {
        habitDao.insertHabits(habits.map { Habit.toHabitEntity(it) })
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.upsertHabit(Habit.toHabitEntity(habit))
    }
}
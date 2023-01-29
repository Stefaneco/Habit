package com.example.habit.domain

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.model.dto.HabitName
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.atTime

class HabitRepository(db: AppDatabase) : IHabitRepository {
    private val habitDao = db.habitDao()
    private val habitHistoryDao = db.habitHistoryDao()
    private val categoryDao = db.habitCategoryDao()

    override suspend fun getAllHabits() : List<Habit> {
        return habitDao.getAllHabitsDto(false).map { Habit.fromHabitDto(it) }
    }

    override suspend fun insertHabit(habit: Habit) : Long{
        val isCategoryInDb = categoryDao.isCategoryNameInDb(habit.category.name)
        if(!isCategoryInDb) {
            val categoryId = categoryDao.insertHabitCategory(HabitCategory.toHabitCategoryEntity(habit.category))
            habit.category.id = categoryId
        }
        else if(habit.category.id == 0L){
            val category = categoryDao.getHabitCategoryByName(habit.category.name)
            habit.category.id = category.id
        }
        return habitDao.insertHabit(Habit.toHabitEntity(habit))
    }

    override suspend fun upsertHabitHistoryItem(habitHistoryItem: HabitHistoryItem){
        return habitHistoryDao.upsertHabitHistoryItem(HabitHistoryItem.toHabitHistoryItemEntity(habitHistoryItem))
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
        //habitDao.deleteHabitById(habitId)
    }

    override suspend fun getAllCategories(): List<HabitCategory> {
        return categoryDao.getAllCategories().map { HabitCategory.fromHabitCategoryEntity(it) }
    }

    override suspend fun getHabitsNamesByCategory(categoryId: Long): List<HabitName> {
        return habitDao.getHabitNamesByCategoryId(categoryId).map { HabitName.fromHabitNameDto(it) }
    }

    override suspend fun getHabit(habitId: Long): Habit {
        return Habit.fromHabitDto(habitDao.getHabitDto(habitId, false))
    }

    override suspend fun getHabitHistoryItems(
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtos(fromTimestamp,toTimestamp)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun getHabitHistoryItemsByCategoryId(fromTimestamp: Long, toTimestamp: Long, categoryId: Long): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtosByCategoryId(fromTimestamp,toTimestamp,categoryId)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun getHabitHistoryItemsByHabitId(fromTimestamp: Long, toTimestamp: Long, habitId: Long): List<HabitHistoryItem> {
        return habitHistoryDao.getHabitHistoryItemDtosByHabitId(fromTimestamp,toTimestamp,habitId)
            .map { HabitHistoryItem.fromHabitHistoryItemDto(it) }
    }

    override suspend fun insertHabitHistoryItems(habitItems: List<HabitHistoryItem>) {
        habitHistoryDao.insertAllHabitHistoryItems(habitItems.map { HabitHistoryItem.toHabitHistoryItemEntity(it) })
    }

    override suspend fun updateHabits(habits: List<Habit>) {
        habitDao.insertHabits(habits.map { Habit.toHabitEntity(it) })
    }

    override suspend fun updateHabit(habit: Habit) {
        /*
        if time changed
            - update all future habitHistoryItems
            - update nextOccurrence
        update category
        update habit
        */

        //if time changed
        if(DateTimeUtil.fromEpochMillis(habit.start).time != DateTimeUtil.fromEpochMillis(habit.nextOccurrence).time){
            //update all future habitHistoryItems
            val futureHistoryItems = habitHistoryDao.getHabitHistoryItemsByHabitId(
                from = DateTimeUtil.nowEpochMillis(),
                habitId = habit.id
            )
            val newFutureHistoryItems = futureHistoryItems.map {
                val newDateTime = DateTimeUtil.fromEpochMillis(it.dateTimeTimestamp).date
                    .atTime(DateTimeUtil.fromEpochMillis(habit.start).time)
                it.copy(dateTimeTimestamp = DateTimeUtil.toEpochMillis(newDateTime))
            }
            habitHistoryDao.upsertHabitHistoryItems(newFutureHistoryItems)

            //update nextOccurrence
            val newNextOccurrenceDateTime =
                DateTimeUtil.fromEpochMillis(habit.nextOccurrence).date
                    .atTime(DateTimeUtil.fromEpochMillis(habit.start).time)
            habit.nextOccurrence = DateTimeUtil.toEpochMillis(newNextOccurrenceDateTime)
        }

        //category update
        val isCategoryInDb = categoryDao.isCategoryNameInDb(habit.category.name)
        if(!isCategoryInDb) {
            val categoryId = categoryDao.insertHabitCategory(HabitCategory.toHabitCategoryEntity(habit.category))
            habit.category.id = categoryId
        }
        else if(habit.category.id == 0L){
            val category = categoryDao.getHabitCategoryByName(habit.category.name)
            habit.category.id = category.id
        }

        //habit update
        habitDao.upsertHabit(Habit.toHabitEntity(habit))
    }
}
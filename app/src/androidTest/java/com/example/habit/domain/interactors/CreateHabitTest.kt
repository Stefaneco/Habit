package com.example.habit.domain.interactors

import com.example.habit.data.room.AppDatabase
import com.example.habit.data.room.entities.HabitCategoryEntity
import com.example.habit.domain.Repetition
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.notification.FakeAlarmScheduler
import com.example.habit.domain.notification.alarm.IAlarmScheduler
import com.example.habit.domain.util.DateTimeUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class CreateHabitTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject lateinit var db: AppDatabase
    @Inject lateinit var createHabit: CreateHabit
    @Inject lateinit var alarmScheduler : IAlarmScheduler

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    /*
    New category is inserted into db
    new category id is assigned to habit
    habit is inserted into db
    new habitHistoryItems starting from habit start to currentDay + 1 are inserted into db
    notifications are scheduled for future habitHistoryItems
     */
    fun createHabitWithNewCategory(){
        val currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 9,
            minute = 0
        )
        val startDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 10,
            minute = 0
        )
        val start = DateTimeUtil.toEpochMillis(startDateTime)
        val categoryName = "Category"
        val category = HabitCategory(0, categoryName)
        val newHabit = Habit(
            name = "Habit",
            start = start,
            nextOccurrence = start + DateTimeUtil.DAY_IN_MILLIS,
            repetition = Repetition.DAILY,
            category = category
        )

        runTest {
            val newHabitId = createHabit(newHabit, currentDateTime)
            val insertedHabit = db.habitDao().getHabit(newHabitId)
            val insertedCategory = db.habitCategoryDao().getHabitCategoryByName(categoryName)
            val insertedHistoryItems = db.habitHistoryDao().getHabitHistoryItemDtosByHabitId(newHabitId)
                .sortedBy { it.dateTimeTimestamp }
            val currentReminders = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders().sortedByDescending { it.time }

            assertNotNull(insertedCategory)
            assertEquals(insertedCategory.id, insertedHabit.categoryId)
            assertEquals(2, insertedHistoryItems.size)
            assertEquals(2, currentReminders.size)
            for (i in insertedHistoryItems.indices){
                assertEquals(insertedHistoryItems[i].dateTimeTimestamp, start + DateTimeUtil.DAY_IN_MILLIS * i)
            }
            for (i in currentReminders.indices){
                assertEquals(newHabit.name, currentReminders[i].habitName)
                assertEquals(
                    currentDateTime.date.plus(DatePeriod(days = 1-i)).atTime(startDateTime.time),
                    DateTimeUtil.fromEpochMillis(currentReminders[i].time)
                )
            }
        }
    }

    @Test
    /*
    No category is inserted into db
    existing category id is assigned to habit
    habit is inserted into db
    new habitHistoryItems starting from habit start to currentDay + 1 are inserted into db
    notifications are scheduled for future habitHistoryItems
     */
    fun createHabitWithExistingCategory(){
        val currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 9,
            minute = 0
        )
        val startDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 10,
            minute = 0
        )
        val start = DateTimeUtil.toEpochMillis(startDateTime)
        val categoryName = "Category"
        val category = HabitCategory(0, categoryName)
        val newHabit = Habit(
            name = "Habit",
            start = start,
            nextOccurrence = start + DateTimeUtil.DAY_IN_MILLIS,
            repetition = Repetition.DAILY,
            category = category
        )
        val categoryId = 10L

        runTest {
            db.habitCategoryDao().insertHabitCategory(HabitCategoryEntity(id = categoryId, name = categoryName))

            val newHabitId = createHabit(newHabit, currentDateTime)
            val insertedHabit = db.habitDao().getHabit(newHabitId)
            val allCategories = db.habitCategoryDao().getAllCategories()
            val insertedHistoryItems = db.habitHistoryDao().getHabitHistoryItemDtosByHabitId(newHabitId)
                .sortedBy { it.dateTimeTimestamp }
            val currentReminders = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders().sortedByDescending { it.time }

            assertEquals(1, allCategories.size)
            assertEquals(categoryId, insertedHabit.categoryId)
            assertEquals(2, insertedHistoryItems.size)
            assertEquals(2, currentReminders.size)
            for (i in insertedHistoryItems.indices){
                assertEquals(insertedHistoryItems[i].dateTimeTimestamp, start + DateTimeUtil.DAY_IN_MILLIS * i)
            }

            for (i in currentReminders.indices){
                assertEquals(newHabit.name, currentReminders[i].habitName)
                assertEquals(
                    currentDateTime.date.plus(DatePeriod(days = 1-i)).atTime(startDateTime.time),
                    DateTimeUtil.fromEpochMillis(currentReminders[i].time)
                )
            }
        }
    }

    @Test
    /*
    habit is inserted into db
    no new habitHistoryItems are added
    none notifications are scheduled
     */
    fun createHabitWithStartDateMoreThanOneDayInFuture(){
        val currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 9,
            minute = 0
        )
        val startDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 8,
            hour = 10,
            minute = 0
        )
        val start = DateTimeUtil.toEpochMillis(startDateTime)
        val categoryName = "Category"
        val category = HabitCategory(0, categoryName)
        val newHabit = Habit(
            name = "Habit",
            start = start,
            nextOccurrence = start + DateTimeUtil.DAY_IN_MILLIS,
            repetition = Repetition.DAILY,
            category = category
        )

        runTest {
            val newHabitId = createHabit(newHabit, currentDateTime)
            val insertedHabit = db.habitDao().getHabit(newHabitId)
            val insertedCategory = db.habitCategoryDao().getHabitCategoryByName(categoryName)
            val insertedHistoryItems = db.habitHistoryDao().getHabitHistoryItemDtosByHabitId(newHabitId)
                .sortedBy { it.dateTimeTimestamp }
            val currentReminders = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders().sortedByDescending { it.time }

            assertNotNull(insertedCategory)
            assertEquals(insertedCategory.id, insertedHabit.categoryId)
            assertEquals(0, insertedHistoryItems.size)
            assertEquals(0, currentReminders.size)
        }
    }

    @Test
            /*
            habit is inserted into db
            new habitHistoryItems starting from habit start to currentDay + 1 are inserted into db
            notifications are scheduled for future habitHistoryItems
             */
    fun createHabitWithStartDateInThePast(){
        val currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 10,
            hour = 9,
            minute = 0
        )
        val startDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 8,
            hour = 10,
            minute = 0
        )
        val start = DateTimeUtil.toEpochMillis(startDateTime)
        val categoryName = "Category"
        val category = HabitCategory(0, categoryName)
        val newHabit = Habit(
            name = "Habit",
            start = start,
            nextOccurrence = start + DateTimeUtil.DAY_IN_MILLIS,
            repetition = Repetition.DAILY,
            category = category
        )

        runTest {
            val newHabitId = createHabit(newHabit, currentDateTime)
            val insertedHabit = db.habitDao().getHabit(newHabitId)
            val insertedCategory = db.habitCategoryDao().getHabitCategoryByName(categoryName)
            val insertedHistoryItems = db.habitHistoryDao().getHabitHistoryItemDtosByHabitId(newHabitId)
                .sortedBy { it.dateTimeTimestamp }
            val currentReminders = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders().sortedByDescending { it.time }

            assertNotNull(insertedCategory)
            assertEquals(insertedCategory.id, insertedHabit.categoryId)
            assertEquals(4, insertedHistoryItems.size)
            assertEquals(2, currentReminders.size)
            for (i in insertedHistoryItems.indices){
                assertEquals(insertedHistoryItems[i].dateTimeTimestamp, start + DateTimeUtil.DAY_IN_MILLIS * i)
            }
            for (i in currentReminders.indices){
                assertEquals(newHabit.name, currentReminders[i].habitName)
                assertEquals(
                    currentDateTime.date.plus(DatePeriod(days = 1-i)).atTime(startDateTime.time),
                    DateTimeUtil.fromEpochMillis(currentReminders[i].time)
                )
            }
        }
    }

    @Test
            /*
            habit is inserted into db
            new habitHistoryItems starting from habit start to currentDay + 1 are inserted into db
            notifications are scheduled for future habitHistoryItems
             */
    fun createHabitWithStartDateTomorrow(){
        val currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 9,
            minute = 0
        )
        val startDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 2,
            hour = 10,
            minute = 0
        )
        val start = DateTimeUtil.toEpochMillis(startDateTime)
        val categoryName = "Category"
        val category = HabitCategory(0, categoryName)
        val newHabit = Habit(
            name = "Habit",
            start = start,
            nextOccurrence = start + DateTimeUtil.DAY_IN_MILLIS,
            repetition = Repetition.DAILY,
            category = category
        )

        runTest {
            val newHabitId = createHabit(newHabit, currentDateTime)
            val insertedHabit = db.habitDao().getHabit(newHabitId)
            val insertedCategory = db.habitCategoryDao().getHabitCategoryByName(categoryName)
            val insertedHistoryItems = db.habitHistoryDao().getHabitHistoryItemDtosByHabitId(newHabitId)
                .sortedBy { it.dateTimeTimestamp }
            val currentReminders = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders().sortedByDescending { it.time }

            assertNotNull(insertedCategory)
            assertEquals(insertedCategory.id, insertedHabit.categoryId)
            assertEquals(1, insertedHistoryItems.size)
            assertEquals(1, currentReminders.size)
            for (i in insertedHistoryItems.indices){
                assertEquals(insertedHistoryItems[i].dateTimeTimestamp, start + DateTimeUtil.DAY_IN_MILLIS * i)
            }
            for (i in currentReminders.indices){
                assertEquals(newHabit.name, currentReminders[i].habitName)
                assertEquals(
                    currentDateTime.date.plus(DatePeriod(days = 1-i)).atTime(startDateTime.time),
                    DateTimeUtil.fromEpochMillis(currentReminders[i].time)
                )
            }
        }
    }
}
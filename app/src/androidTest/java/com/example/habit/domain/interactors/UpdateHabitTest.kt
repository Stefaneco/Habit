package com.example.habit.domain.interactors

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.Repetition
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.notification.FakeAlarmScheduler
import com.example.habit.domain.notification.alarm.IAlarmScheduler
import com.example.habit.domain.util.DateTimeUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Long.max
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UpdateHabitTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var createHabit: CreateHabit
    @Inject
    lateinit var alarmScheduler : IAlarmScheduler
    @Inject
    lateinit var updateHabit: UpdateHabit

    private lateinit var habit : Habit
    private lateinit var currentDateTime: LocalDateTime

    @Before
    fun setUp() {
        hiltRule.inject()

        currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 2,
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

        habit = Habit(
            id = 1,
            name = "Habit",
            start = start,
            nextOccurrence = max(
                start,
                DateTimeUtil.toEpochMillis(currentDateTime) + DateTimeUtil.DAY_IN_MILLIS * 2
            ),
            repetition = Repetition.DAILY,
            category = category
        )
        runBlocking {
            createHabit(habit,currentDateTime)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    /*
    new category is inserted into db
    new category id is assigned to habit
     */
    fun updateHabitWithNewCategory(){
        val newCategory = HabitCategory(0, "NewCategory")
        runTest {
            updateHabit(habit.copy(category = newCategory), currentDateTime)
            val updatedHabit = db.habitDao().getHabit(habit.id)
            val allCategories = db.habitCategoryDao().getAllCategories()
            assertEquals(2, allCategories.size)
            assertEquals(2, updatedHabit.categoryId)
        }
    }

    @Test
    /*
    existing category id is assigned to habit
    no category is inserted into db
     */
    fun updateHabitWithExistingCategory(){
        val existingCategory = HabitCategory(0, "ExistingCategory")
        runTest {
            db.habitCategoryDao().insertHabitCategory(HabitCategory.toHabitCategoryEntity(existingCategory))
            updateHabit(habit.copy(category = existingCategory), currentDateTime)
            val updatedHabit = db.habitDao().getHabit(habit.id)
            val allCategories = db.habitCategoryDao().getAllCategories()
            assertEquals(2, allCategories.size)
            assertEquals(2, updatedHabit.categoryId)
        }
    }

    @Test
    fun updateHabitPlannedTimeToFutureHourFromFutureHour(){
        val newTime = LocalTime(20,0)
        val newDateTime = DateTimeUtil.fromEpochMillis(habit.start).date.atTime(newTime)
        val newDateTimeTimestamp = DateTimeUtil.toEpochMillis(newDateTime)

        runTest {
            updateHabit(habit.copy(start = newDateTimeTimestamp), currentDateTime)

            val updatedHabit = db.habitDao().getHabit(habit.id)
            val alarms = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders()

            assertEquals(newDateTimeTimestamp, updatedHabit.start)
            assertEquals(
                DateTimeUtil.fromEpochMillis(habit.nextOccurrence).date.atTime(newTime),
                DateTimeUtil.fromEpochMillis(updatedHabit.nextOccurrence)
            )
            assertEquals(2, alarms.size)
            for (alarm in alarms){
                assertEquals(newTime, DateTimeUtil.fromEpochMillis(alarm.time).time)
            }
        }
    }

    @Test
    fun updateHabitPlannedTimeToPastHourFromFutureHour(){
        val newTime = LocalTime(1,0)
        val newDateTime = DateTimeUtil.fromEpochMillis(habit.start).date.atTime(newTime)
        val newDateTimeTimestamp = DateTimeUtil.toEpochMillis(newDateTime)

        runTest {
            updateHabit(habit.copy(start = newDateTimeTimestamp), currentDateTime)

            val updatedHabit = db.habitDao().getHabit(habit.id)
            val alarms = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders()

            assertEquals(newDateTimeTimestamp, updatedHabit.start)
            assertEquals(
                DateTimeUtil.fromEpochMillis(habit.nextOccurrence).date.atTime(newTime),
                DateTimeUtil.fromEpochMillis(updatedHabit.nextOccurrence)
            )
            assertEquals(1, alarms.size)
            for (alarm in alarms){
                assertEquals(newTime, DateTimeUtil.fromEpochMillis(alarm.time).time)
            }
        }
    }

    @Test
    fun updateHabitPlannedTimeToPastHourFromPastHour(){
        currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 15,
            minute = 0
        )
        (alarmScheduler as FakeAlarmScheduler).removeRemindersBeforeDateTime(currentDateTime)
        val newTime = LocalTime(1,0)
        val newDateTime = DateTimeUtil.fromEpochMillis(habit.start).date.atTime(newTime)
        val newDateTimeTimestamp = DateTimeUtil.toEpochMillis(newDateTime)

        runTest {
            updateHabit(habit.copy(start = newDateTimeTimestamp), currentDateTime)

            val updatedHabit = db.habitDao().getHabit(habit.id)
            val alarms = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders()

            assertEquals(newDateTimeTimestamp, updatedHabit.start)
            assertEquals(
                DateTimeUtil.fromEpochMillis(habit.nextOccurrence).date.atTime(newTime),
                DateTimeUtil.fromEpochMillis(updatedHabit.nextOccurrence)
            )
            assertEquals(1, alarms.size)
            for (alarm in alarms){
                assertEquals(newTime, DateTimeUtil.fromEpochMillis(alarm.time).time)
            }
        }
    }

    @Test
    fun updateHabitPlannedTimeToFutureHourFromPastHour(){
        currentDateTime = LocalDateTime(
            year = 2023,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 15,
            minute = 0
        )
        (alarmScheduler as FakeAlarmScheduler).removeRemindersBeforeDateTime(currentDateTime)
        val newTime = LocalTime(20,0)
        val newDateTime = DateTimeUtil.fromEpochMillis(habit.start).date.atTime(newTime)
        val newDateTimeTimestamp = DateTimeUtil.toEpochMillis(newDateTime)

        runTest {
            updateHabit(habit.copy(start = newDateTimeTimestamp), currentDateTime)

            val updatedHabit = db.habitDao().getHabit(habit.id)
            val alarms = (alarmScheduler as FakeAlarmScheduler).getCurrentReminders()

            assertEquals(newDateTimeTimestamp, updatedHabit.start)
            assertEquals(
                DateTimeUtil.fromEpochMillis(habit.nextOccurrence).date.atTime(newTime),
                DateTimeUtil.fromEpochMillis(updatedHabit.nextOccurrence)
            )
            assertEquals(1, alarms.size)
            for (alarm in alarms){
                assertEquals(newTime, DateTimeUtil.fromEpochMillis(alarm.time).time)
            }
        }
    }

}
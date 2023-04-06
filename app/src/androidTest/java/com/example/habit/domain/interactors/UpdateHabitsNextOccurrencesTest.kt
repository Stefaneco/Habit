package com.example.habit.domain.interactors

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.Repetition
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.domain.util.rangeTo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UpdateHabitsNextOccurrencesTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var updateHabitsNextOccurrences: UpdateHabitsNextOccurrences

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

        habit = Habit(
            id = 1,
            name = "Habit",
            start = startDateTime,
            nextOccurrence = startDateTime,
            repetition = Repetition.DAILY,
            category = HabitCategory(0, "Category")
        )
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun executeDailyForAWeekWithDailyHabit(){
        runBlocking {
            for (date in currentDateTime.date..currentDateTime.date.plus(DatePeriod(days = 7))){
                updateHabitsNextOccurrences(listOf(habit), DateTimeUtil.dayEnd(date.plus(DatePeriod(days = 1))))
            }
            val timestamps = db.habitHistoryDao().getHabitHistoryItemDtosByHabitId(habit.id).map { it.dateTimeTimestamp }

            for (date in currentDateTime.date.plus(DatePeriod(days = 1))..currentDateTime.date.plus(DatePeriod(days = 8))){
                assert(timestamps.contains(
                    DateTimeUtil.toEpochMillis(date.atTime(habit.start.time))
                ))
            }
        }
    }

    @Test
    fun executeHourlyForAWeekWithDailyHabit(){

    }

    @Test
    fun executeWithNextOccurrenceWeekInThePast(){

    }

    @Test
    fun executeWithNextOccurrenceWeekInTheFuture(){

    }
}
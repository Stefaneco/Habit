package com.example.habit.domain.util

import com.example.habit.R
import com.example.habit.domain.util.model.StringResource
import kotlinx.datetime.*


object DateTimeUtil {

    const val DAY_IN_MILLIS = 86400000
    const val HOUR_IN_MILLIS = 3600000

    fun dayStartEpochMillis() : Long {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            .date.atTime(0,0,0,0)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun dayStartEpochMillis(date: LocalDate) : Long {
        return date.atTime(0,0,0,0)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun dayEndEpochMillis(): Long {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            .date.atTime(23,59,59,999)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun dayEndEpochMillis(date: LocalDate): Long {
        return date.atTime(23,59,59,999)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun dayEnd(date: LocalDate): LocalDateTime {
        return date.atTime(23,59,59,999)
    }

    fun dayEnd(dateTime: LocalDateTime): LocalDateTime {
        return dateTime.date.atTime(23,59,59,999)
    }

    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun nowPlus(days: Int = 0, hours: Int = 0) : LocalDateTime {
        val dateInstant = Clock.System.now()
        val systemTZ = TimeZone.currentSystemDefault()
        return dateInstant.plus(DateTimePeriod(days = days, hours = hours), systemTZ).toLocalDateTime(systemTZ)
    }

    fun nowEpochMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun toEpochMillis(dateTime: LocalDateTime): Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun fromEpochMillis(millis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun formatDate(millis: Long): String {
        return formatDate(fromEpochMillis(millis))
    }

    fun formatDate(dateTime: LocalDateTime): String {
        return formatDate(dateTime.date)
    }

    fun formatFriendlyDate(millis: Long): StringResource {
        return formatFriendlyDate(fromEpochMillis(millis).date)
    }

    fun formatFriendlyDate(dateTime: LocalDateTime): StringResource {
        return formatFriendlyDate(dateTime.date)
    }

    fun formatFriendlyDate(dateTime: LocalDate): StringResource {
        return when(now().date.daysUntil(dateTime)){
            -1 -> { StringResource(R.string.yesterday,"Yesterday") }
            0 -> { StringResource(R.string.today,"Today") }
            1 -> { StringResource(R.string.tomorrow,"Tomorrow") }
            else -> { StringResource(null, formatDate(dateTime)) }
        }
    }

    fun formatDate(date: LocalDate, getYear: Boolean = true): String {
        val month = if(date.monthNumber < 10) "0${date.monthNumber}" else date.monthNumber
        val day = if(date.dayOfMonth < 10) "0${date.dayOfMonth}" else date.dayOfMonth
        val year = date.year

        return buildString {
            append(day)
            append(".")
            append(month)
            if(getYear) {
                append(".")
                append(year)
            }
        }
    }

    fun formatShortDate(date: LocalDate): String {
        val month = if(date.monthNumber < 10) "0${date.monthNumber}" else date.monthNumber
        val day = if(date.dayOfMonth < 10) "0${date.dayOfMonth}" else date.dayOfMonth
        val year = date.year.toString().takeLast(2)

        return buildString {
            append(day)
            append(".")
            append(month)
            append(".")
            append(year)
        }
    }

    fun formatTime(dateTime: LocalDateTime): String {
        return formatTime(dateTime.time)
    }

    fun formatTime(time: LocalTime): String {
        val hour = if(time.hour < 10) "0${time.hour}" else time.hour
        val minute = if(time.minute < 10) "0${time.minute}" else time.minute
        return  buildString {
            append(hour)
            append(":")
            append(minute)
        }
    }

    fun formatTime(start: Long): String {
        val time = fromEpochMillis(start)
        val hour = if(time.hour < 10) "0${time.hour}" else time.hour
        val minute = if(time.minute < 10) "0${time.minute}" else time.minute
        return  buildString {
            append(hour)
            append(":")
            append(minute)
        }
    }
}
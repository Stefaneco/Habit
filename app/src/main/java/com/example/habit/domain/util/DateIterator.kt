package com.example.habit.domain.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class DateIterator(
    val startDate: LocalDate,
    val endDateInclusive: LocalDate,
    val stepDays: Long
): Iterator<LocalDate> {

    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive
    override fun next(): LocalDate {
        val next = currentDate
        currentDate = currentDate.plus(DatePeriod(days = stepDays.toInt()))
        return next
    }
}
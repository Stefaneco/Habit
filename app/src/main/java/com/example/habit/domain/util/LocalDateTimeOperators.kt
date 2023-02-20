package com.example.habit.domain.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.plus

operator fun LocalDateTime.plusAssign(datePeriod: DatePeriod) {
    this.date.plus(datePeriod).atTime(this.time)
}

operator fun LocalDateTime.plus(datePeriod: DatePeriod) {
    this.date.plus(datePeriod).atTime(this.time)
}
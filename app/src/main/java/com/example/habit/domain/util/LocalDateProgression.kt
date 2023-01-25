package com.example.habit.domain.util

import kotlinx.datetime.LocalDate

//Created using blog post
//https://www.netguru.com/blog/traversing-through-dates-with-kotlin-range-expressions
operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
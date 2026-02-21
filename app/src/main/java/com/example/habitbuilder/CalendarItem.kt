package com.example.habitbuilder

import java.util.Calendar

sealed class CalendarItem {
    data class Day(val date: Calendar, val status: Status) : CalendarItem()
    object Empty : CalendarItem()
}
package com.example.habitbuilder.data.converter

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarConverter {
    @TypeConverter
    fun fromCalendar(date: Calendar?): Long? =
        date?.timeInMillis

    @TypeConverter
    fun toCalendar(time: Long?): Calendar? =
        time?.let { Calendar.getInstance().apply { timeInMillis = it } }
}
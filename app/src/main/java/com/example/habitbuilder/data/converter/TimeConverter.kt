//package com.example.habitbuilder.data.converter
//
//import androidx.room.TypeConverter
//import java.time.LocalTime
//
//class TimeConverter{
//    @TypeConverter
//    fun fromTime(time: LocalTime?): Int? =
//        time?.toSecondOfDay()
//
//    @TypeConverter
//    fun toTime(secondOfDay: Int?) =
//        secondOfDay?.let { LocalTime.ofSecondOfDay(secondOfDay.toLong()) }
//}

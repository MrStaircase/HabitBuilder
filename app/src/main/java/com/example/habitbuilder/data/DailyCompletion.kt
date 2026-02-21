package com.example.habitbuilder.data

import java.util.Calendar

data class DailyCompletion(
    val date: Calendar,
    val completedActions: Int
)
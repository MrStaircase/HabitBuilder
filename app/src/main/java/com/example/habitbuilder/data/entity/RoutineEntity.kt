package com.example.habitbuilder.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "routines",
    indices = [Index("triggerTime")]
)
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "triggerTime") var triggerTime: Calendar,
    @ColumnInfo(name = "creationDate") val creationDate: Calendar
)

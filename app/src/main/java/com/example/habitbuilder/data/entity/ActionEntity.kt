package com.example.habitbuilder.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "actions",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class ActionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "routineId") val routineId: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "durationMinutes") var durationMinutes: Int = 0,
    @ColumnInfo(name = "creationDate") val creationDate: Calendar
)
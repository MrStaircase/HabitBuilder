package com.example.habitbuilder.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "completion",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActionEntity::class,
            parentColumns = ["id"],
            childColumns = ["actionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId"), Index("actionId"), Index("date")]
)
data class CompletionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "complete_id") val id: Int = 0,
    @ColumnInfo(name = "routineId") val routineId: Int,
    @ColumnInfo(name = "actionId") val actionId: Int,
    @ColumnInfo(name = "date") val date: Calendar
)

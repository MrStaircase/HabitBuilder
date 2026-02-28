package com.example.habitbuilder.data

import com.example.habitbuilder.data.entity.ActionEntity

data class Action(
    val id: Int,
    val routineId: Int,
    val description: String,
    val durationMinutes: Int
){
    constructor(entity: ActionEntity) : this(
        id = entity.id,
        routineId = entity.routineId,
        description = entity.description,
        durationMinutes = entity.durationMinutes
    )
}
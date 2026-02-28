package com.example.habitbuilder.data

import com.example.habitbuilder.data.entity.ActionEntity

data class Action(
    val id: Int,
    val description: String,
    val durationMinutes: Int
){
    constructor(entity: ActionEntity) : this(
        id = entity.id,
        description = entity.description,
        durationMinutes = entity.durationMinutes
    )
}
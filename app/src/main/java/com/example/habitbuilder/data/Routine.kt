package com.example.habitbuilder.data

import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.entity.RoutineEntity
import java.util.Calendar

data class Routine(
    val id: Int,
    val name: String,
    val triggerTime: Calendar,
    val actions: List<Action>
){
    constructor(
        routineEntity: RoutineEntity,
        actionEntities: List<ActionEntity>
    ) : this(
        id = routineEntity.id,
        name = routineEntity.name,
        triggerTime = routineEntity.triggerTime,
        actions = actionEntities.map { Action(it.id, it.description, it.durationMinutes) }
    )
}
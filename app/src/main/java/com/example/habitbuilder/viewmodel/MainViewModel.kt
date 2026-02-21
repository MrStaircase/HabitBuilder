package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.entity.CompletionEntity
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.CompletionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import com.example.habitbuilder.notification.NotificationHelper
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel : ViewModel() {
    private val _routines = MutableLiveData<List<RoutineEntity>>()
    val routines: LiveData<List<RoutineEntity>> = _routines

    private val _routineId = MutableLiveData<Int>()
    val routineId: LiveData<Int> = _routineId

    private val _routineName = MutableLiveData<String>()
    val routineName: LiveData<String> = _routineName

    private val _triggerTime = MutableLiveData<Calendar>().apply { postValue(Calendar.getInstance()) }
    val triggerTime: LiveData<Calendar> = _triggerTime

    fun loadRoutines(context: Context) {
        viewModelScope.launch {
            _routines.postValue(RoutineRepository.getAll(context))
        }
    }

    fun setRoutineName(name: String){
        _routineName.postValue(name)
    }

    fun setTriggerTime(time: Calendar){
        _triggerTime.postValue(time)
    }

    fun createRoutine(context: Context, routine: RoutineEntity){
        viewModelScope.launch{
            val newRoutineId = RoutineRepository.insert(context, routine).toInt()
            _routineId.postValue(newRoutineId)
            NotificationHelper.scheduleFirstAction(context, routine)
        }
    }

    fun createTestData(context: Context) {
        viewModelScope.launch {
            fun makeCalendar(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0): Calendar {
                return Calendar.getInstance().apply {
                    set(year, month - 1, day, hour, minute, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }

            val routine1Time = makeCalendar(2026, 1, 15, 8, 35)
            val routine1 = RoutineEntity(
                id = 666,
                name = "Test1",
                triggerTime = routine1Time,
                creationDate = makeCalendar(2026, 1, 15)
            )
            RoutineRepository.insert(context, routine1)

            val action1 =
                ActionEntity(888, routine1.id, "Action1", creationDate = makeCalendar(2026, 1, 15))
            val action2 =
                ActionEntity(889, routine1.id, "Action2", creationDate = makeCalendar(2026, 1, 15))
            val action3 =
                ActionEntity(890, routine1.id, "Action3", creationDate = makeCalendar(2026, 1, 15))
            val action4 =
                ActionEntity(891, routine1.id, "Action4", creationDate = makeCalendar(2026, 1, 20))

            listOf(action1, action2, action3, action4).forEach { ActionRepository.insert(context, it) }

            val routine2Time = makeCalendar(2026, 1, 22, 10, 42)
            val routine2 = RoutineEntity(
                id = 999,
                name = "Test2",
                triggerTime = routine2Time,
                creationDate = makeCalendar(2026, 1, 22)
            )
            RoutineRepository.insert(context, routine2)

            val action5 =
                ActionEntity(892, routine2.id, "Action1", creationDate = makeCalendar(2026, 1, 22))
            val action6 =
                ActionEntity(893, routine2.id, "Action2", creationDate = makeCalendar(2026, 1, 22))
            val action7 =
                ActionEntity(894, routine2.id, "Action3", creationDate = makeCalendar(2026, 1, 23))

            listOf(action5, action6, action7).forEach { ActionRepository.insert(context, it) }

            val completions = listOf(
                CompletionEntity(666, routine1.id, action1.id, makeCalendar(2026, 1, 22)),
                CompletionEntity(667, routine1.id, action2.id, makeCalendar(2026, 1, 22)),
                CompletionEntity(668, routine1.id, action3.id, makeCalendar(2026, 1, 22)),
                CompletionEntity(669, routine1.id, action4.id, makeCalendar(2026, 1, 22)),
                CompletionEntity(670, routine1.id, action1.id, makeCalendar(2026, 1, 23)),
                CompletionEntity(671, routine1.id, action2.id, makeCalendar(2026, 1, 23)),
                CompletionEntity(672, routine1.id, action3.id, makeCalendar(2026, 1, 23)),
                CompletionEntity(673, routine1.id, action1.id, makeCalendar(2026, 1, 15)),
                CompletionEntity(674, routine1.id, action2.id, makeCalendar(2026, 1, 15)),
                CompletionEntity(675, routine1.id, action3.id, makeCalendar(2026, 1, 15))
            )

            completions.forEach { CompletionRepository.insert(context, it) }
        }
    }
}
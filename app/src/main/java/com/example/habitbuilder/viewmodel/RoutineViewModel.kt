package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import com.example.habitbuilder.notification.NotificationHelper
import kotlinx.coroutines.launch
import java.util.Calendar

class RoutineViewModel() : ViewModel(){
    private val _actionId = MutableLiveData<Int>()
    val actionId: LiveData<Int> = _actionId

    private val _routine = MutableLiveData<Pair<RoutineEntity, List<ActionEntity>>>()
    val routine: LiveData<Pair<RoutineEntity, List<ActionEntity>>> = _routine

    private val _actionDescription = MutableLiveData<String>()
    val actionDescription: LiveData<String> = _actionDescription

    private val _actionDuration = MutableLiveData<Int>()
    val actionDuration: LiveData<Int> = _actionDuration


    fun loadRoutine(context: Context, routineId: Int) {
        if (_routine.value != null) return
        viewModelScope.launch {
            val routineEntity = RoutineRepository.get(context, routineId)
            routineEntity?.let {
                val routineActions = ActionRepository.getAll(context, routineId)
                _routine.postValue(Pair(routineEntity, routineActions))
            }
        }
    }

    fun saveRoutine(context: Context, name: String) {
        _routine.value?.let { (routine, _) ->
            routine.name = name
            viewModelScope.launch { RoutineRepository.update(context, routine) }
            NotificationHelper.scheduleFirstAction(context, routine)
        }
    }

    fun setRoutineName(context: Context, name: String){
        _routine.value?.let{ (routine, _) ->
            routine.name = name
            viewModelScope.launch { RoutineRepository.update(context, routine) }
            loadRoutine(context, routine.id)
        }
    }

    fun setTriggerTime(context: Context, time: Calendar){
        _routine.value?.let{ (routine, _) ->
            routine.triggerTime = time
            viewModelScope.launch { RoutineRepository.update(context, routine) }
            loadRoutine(context, routine.id)
        }
    }

    fun setActionDescription(description: String){
        _actionDescription.postValue(description)
    }

    fun setActionDuration(duration: Int?){
        duration?.let {
            _actionDuration.postValue(duration)
        }
    }

    fun createAction(context: Context, action: ActionEntity){
        viewModelScope.launch{
            val newActionId = ActionRepository.insert(context, action).toInt()
            _actionId.postValue(newActionId)
        }
    }

    fun deleteRoutine(context: Context) {
        viewModelScope.launch {
            _routine.value?.let { (routine, _) ->
                ActionRepository.deleteAllByRoutine(context, routine.id)
                RoutineRepository.delete(context, routine)
            }
        }
    }
}

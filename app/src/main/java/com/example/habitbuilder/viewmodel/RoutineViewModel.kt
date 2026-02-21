package com.example.habitbuilder.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import com.example.habitbuilder.notification.NotificationHelper
import com.example.habitbuilder.view.activity.CalendarActivity
import kotlinx.coroutines.launch
import java.util.Calendar

class RoutineViewModel() : ViewModel(){
    var routineId: Int = -1
    lateinit var context: Context

    private val _actionId = MutableLiveData<Int>()
    val actionId: LiveData<Int> = _actionId

    private val _routine = MutableLiveData<RoutineEntity>()
    val routine: LiveData<RoutineEntity> = _routine

    private val _actions = MutableLiveData<List<ActionEntity>>()
    val actions: LiveData<List<ActionEntity>> = _actions

    private val _routineName = MutableLiveData<String>()
    val routineName: LiveData<String> = _routineName

    private val _triggerTime = MutableLiveData<Calendar>()
    val triggerTime: LiveData<Calendar> = _triggerTime

    private val _actionDescription = MutableLiveData<String>()
    val actionDescription: LiveData<String> = _actionDescription

    private val _actionDuration = MutableLiveData<Int>()
    val actionDuration: LiveData<Int> = _actionDuration


    fun loadRoutine() {
        if (_routine.value != null) return
        viewModelScope.launch {
            val routineEntity = RoutineRepository.get(context, routineId)
            routineEntity?.let {
                _routine.postValue(routineEntity)
                _routineName.postValue(routineEntity.name)
                _triggerTime.postValue(routineEntity.triggerTime)
            }
            _actions.postValue(ActionRepository.getAll(context, routineId))
        }
    }

    fun saveRoutine(name: String) {
        _routine.value?.let {
            it.name = name
            viewModelScope.launch { RoutineRepository.update(context, it) }
            NotificationHelper.scheduleFirstAction(context, it)
        }
    }

    fun setRoutineName(name: String){
        _routineName.postValue(name)
    }

    fun setTriggerTime(time: Calendar){
        _triggerTime.postValue(time)
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

    fun deleteRoutine() {
        viewModelScope.launch {
            _routine.value?.let { r ->
                ActionRepository.deleteAllByRoutine(context, r.id)
                RoutineRepository.delete(context, r)
            }
        }
    }

    fun loadCalendar(){
        val intent = Intent(context, CalendarActivity::class.java)
        intent.putExtra("routineId", routineId)
        context.startActivity(intent)
    }
}

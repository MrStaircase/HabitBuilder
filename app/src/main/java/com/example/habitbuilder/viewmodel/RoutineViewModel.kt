package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.habitbuilder.data.Routine
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import com.example.habitbuilder.notification.NotificationHelper
import kotlinx.coroutines.launch
import java.util.Calendar

class RoutineViewModel(private val context: Context) : ViewModel(){

    companion object{
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T{
                val application = checkNotNull(extras[APPLICATION_KEY])
                return RoutineViewModel(application as Context) as T
            }
        }
    }

    private val _actionId = MutableLiveData<Int>()
    val actionId: LiveData<Int> = _actionId

    private val _routine = MutableLiveData<Routine>();
    val routine: LiveData<Routine> = _routine;

    private val _actionDescription = MutableLiveData<String>()
    val actionDescription: LiveData<String> = _actionDescription

    private val _actionDuration = MutableLiveData<Int>()
    val actionDuration: LiveData<Int> = _actionDuration


    fun loadRoutine(routineId: Int) {
        viewModelScope.launch {
            val routineEntity = RoutineRepository.get(context, routineId)
            routineEntity?.let {
                val routineActions = ActionRepository.getAll(context, routineId)
                _routine.postValue(Routine(routineEntity, routineActions))
            }
        }
    }

    fun saveRoutine(name: String, time: Calendar) {
        _routine.value?.let { routine ->
            viewModelScope.launch { RoutineRepository.updateName(context, routine.id, name) }
            viewModelScope.launch { RoutineRepository.updateTriggerTime(context, routine.id, time) }
            loadRoutine(routine.id)
            val updatedRoutine = _routine.value ?: routine
            NotificationHelper.scheduleFirstAction(context, updatedRoutine)
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

    fun createAction(action: ActionEntity){
        viewModelScope.launch{
            val newActionId = ActionRepository.insert(context, action).toInt()
            _actionId.postValue(newActionId)
        }
    }

    fun deleteRoutine() {
        viewModelScope.launch {
            _routine.value?.let { routine ->
                ActionRepository.deleteAllByRoutine(context, routine.id)
                RoutineRepository.delete(context, routine.id)
            }
        }
    }
}

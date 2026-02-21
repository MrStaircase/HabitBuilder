package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import kotlinx.coroutines.launch

class ActionViewModel() : ViewModel() {
    private val _action = MutableLiveData<ActionEntity>()
    val action: LiveData<ActionEntity> = _action
    
    fun loadAction(context: Context, actionId: Int) {
        _action.value?.let {
            viewModelScope.launch {
                val actionEntity = ActionRepository.get(context, actionId)
                actionEntity?.let {
                    _action.postValue(actionEntity)
                }
            }
        }
    }

    fun setActionDescription(context: Context, description: String){
        _action.value?.let{ action ->
            action.description = description
            viewModelScope.launch { ActionRepository.update(context, action) }
            loadAction(context, action.id)
        }
    }

    fun setActionDuration(context: Context, duration: Int?){
        duration?.let{
            _action.value?.let{ action ->
                action.durationMinutes = duration
                viewModelScope.launch { ActionRepository.update(context, action) }
                loadAction(context, action.id)
            }
        }
    }

    fun saveAction(context: Context, description: String, durationMinutes: Int) {
        viewModelScope.launch {
            _action.value?.let {
                it.description = description
                it.durationMinutes = durationMinutes
                ActionRepository.update(context, it)
            }
        }
    }

    fun deleteAction(context: Context) {
        viewModelScope.launch {
            _action.value?.let { ActionRepository.delete(context, it) }
        }
    }
}

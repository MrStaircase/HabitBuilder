package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.data.Action
import com.example.habitbuilder.data.repository.ActionRepository
import kotlinx.coroutines.launch

class ActionViewModel() : ViewModel() {
    private val _action = MutableLiveData<Action>()
    val action: LiveData<Action> = _action

    fun loadAction(context: Context, actionId: Int) {
        viewModelScope.launch {
            val actionEntity = ActionRepository.get(context, actionId)
            actionEntity?.let {
                _action.postValue(Action(actionEntity))
            }
        }
    }

    fun setActionDescription(context: Context, description: String){
        _action.value?.let{ action ->
            viewModelScope.launch { ActionRepository.updateDescription(context, action.id, description) }
            loadAction(context, action.id)
        }
    }

    fun setActionDuration(context: Context, duration: Int?){
        duration?.let{
            _action.value?.let{ action ->
                viewModelScope.launch { ActionRepository.updateDuration(context, action.id, duration) }
                loadAction(context, action.id)
            }
        }
    }

    fun saveAction(context: Context, description: String, durationMinutes: Int) {
        setActionDescription(context, description)
        setActionDuration(context, durationMinutes)
    }

    fun deleteAction(context: Context) {
        viewModelScope.launch {
            _action.value?.let { ActionRepository.delete(context, it.id) }
        }
    }
}

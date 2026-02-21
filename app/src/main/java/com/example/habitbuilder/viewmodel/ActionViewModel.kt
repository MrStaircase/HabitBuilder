package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.repository.ActionRepository
import kotlinx.coroutines.launch

class ActionViewModel() : ViewModel() {
    var actionId: Int = -1
    lateinit var context: Context

    val action = MutableLiveData<ActionEntity>()

    private val _actionDescription = MutableLiveData<String>()
    val actionDescription: LiveData<String> = _actionDescription

    private val _actionDuration = MutableLiveData<Int>()
    val actionDuration: LiveData<Int> = _actionDuration

    fun loadAction() {
        if (action.value != null) return
        viewModelScope.launch {
            val actionEntity = ActionRepository.get(context, actionId)
            action.postValue(actionEntity)
            actionEntity?.let {
                _actionDescription.postValue(it.description)
                _actionDuration.postValue(it.durationMinutes)
            }
        }
    }

    fun setActionDescription(description: String){
        _actionDescription.postValue(description)
    }

    fun setActionDuration(duration: Int?){
        _actionDuration.postValue(duration)
    }

    fun saveAction(description: String, durationMinutes: Int) {
        viewModelScope.launch {
            action.value?.let {
                it.description = description
                it.durationMinutes = durationMinutes
                ActionRepository.update(context, it)
            }
        }
    }

    fun deleteAction() {
        viewModelScope.launch {
            action.value?.let { ActionRepository.delete(context, it) }
        }
    }
}

package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.habitbuilder.data.Action
import com.example.habitbuilder.data.repository.ActionRepository
import kotlinx.coroutines.launch

class ActionViewModel(private val context: Context) : ViewModel() {

    companion object{
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T{
                val application = checkNotNull(extras[APPLICATION_KEY])
                return ActionViewModel(application as Context) as T
            }
        }
    }

    private val _action = MutableLiveData<Action>()
    val action: LiveData<Action> = _action

    fun loadAction(actionId: Int) {
        viewModelScope.launch {
            val actionEntity = ActionRepository.get(context, actionId)
            actionEntity?.let {
                _action.postValue(Action(actionEntity))
            }
        }
    }

    fun saveAction(description: String, durationMinutes: Int) {
        _action.value?.let{ action ->
            viewModelScope.launch { ActionRepository.updateDescription(context, action.id, description) }
            viewModelScope.launch { ActionRepository.updateDuration(context, action.id, durationMinutes) }
            loadAction(action.id)
        }
    }

    fun deleteAction() {
        viewModelScope.launch {
            _action.value?.let { ActionRepository.delete(context, it.id) }
        }
    }
}

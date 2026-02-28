package com.example.habitbuilder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class TempViewModel : ViewModel() {
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> = _duration

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _triggerTime = MutableLiveData<Calendar>()
    val triggerTime: LiveData<Calendar> = _triggerTime

    fun setRoutineName(name: String){
        _name.postValue(name)
    }

    fun setTriggerTime(time: Calendar){
        _triggerTime.postValue(time)
    }

    fun setActionDescription(description: String){
        _description.postValue(description)
    }

    fun setActionDuration(duration: Int?){
        duration?.let{
            _duration.postValue(duration)
        }
    }
}
package com.example.habitbuilder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TempViewModel : ViewModel() {
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> = _duration

    fun setActionDescription(description: String){
        _description.postValue(description)
    }

    fun setActionDuration(duration: Int?){
        duration?.let{
            _duration.postValue(duration)
        }
    }
}
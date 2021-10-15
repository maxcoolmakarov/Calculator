package com.example.calculator

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel : ViewModel() {

    private var expression: String = ""
    private val _expressionState = MutableLiveData<String>()
    val expressionState: LiveData<String> = _expressionState

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "OnCleared")
    }
    fun onNumberClick(number: Int){
        expression += number.toString()
        _expressionState.value = expression
    }
}
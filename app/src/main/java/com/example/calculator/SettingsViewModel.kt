package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _resultPanelState = MutableLiveData<ResultPanelType>(ResultPanelType.LEFT)
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _openResultPanelAction = MutableLiveData<ResultPanelType>()
    val openResultPanelAction: LiveData<ResultPanelType> = _openResultPanelAction

    fun onResultPanelStateChanged(resultPanelType: ResultPanelType) {
        _resultPanelState.value = resultPanelType
    }


    fun openResultPanelAction() {
        _openResultPanelAction.value = _resultPanelState.value
    }

}

enum class ResultPanelType {
    LEFT, RIGHT, HIDE
}
package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.SettingsDao
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDao: SettingsDao
) : ViewModel() {

    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _openResultPanelAction = MutableLiveData<ResultPanelType>()
    val openResultPanelAction: LiveData<ResultPanelType> = _openResultPanelAction

    private val _resultAccuracy = MutableLiveData<Int>()
    val resultAccuracy = _resultAccuracy

    private val _vibrationForce = MutableLiveData<Int>()
    val vibrationForce = _vibrationForce

    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
        }
        _resultAccuracy.value = settingsDao.getResultAccuracy()
        _vibrationForce.value = settingsDao.getVibrationForce()
        Log.d("SettingsInit", "polzunok"+settingsDao.getResultAccuracy().toString())
    }

    fun onResultPanelStateChanged(resultPanelType: ResultPanelType) {
        _resultPanelState.value = resultPanelType
        viewModelScope.launch {
            settingsDao.setResultPanelType(resultPanelType)
        }
    }


    fun openResultPanelAction() {
        _openResultPanelAction.value = _resultPanelState.value
    }

    fun onAccuracyBarChanged(accuracy: Int) {
        _resultAccuracy.value = accuracy
        settingsDao.setResultAccuracy(accuracy)
    }

    fun onVibrationForceChanged(force: Int) {
        _vibrationForce.value = force
        settingsDao.setVibrationForce(force)
    }
}

enum class ResultPanelType {
    LEFT, RIGHT, HIDE
}
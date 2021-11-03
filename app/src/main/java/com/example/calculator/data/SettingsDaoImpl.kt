package com.example.calculator.data

import android.content.SharedPreferences
import android.util.Log
import com.example.calculator.ResultPanelType
import com.example.calculator.domain.SettingsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsDaoImpl(
    private val preferences: SharedPreferences
) : SettingsDao {
    override suspend fun getResultPanelType(): ResultPanelType = withContext(Dispatchers.IO) {
        preferences.getString(RESULT_PANEL_TYPE_KEY, null)
            ?.let { ResultPanelType.valueOf(it) } ?: ResultPanelType.LEFT
    }

    override suspend fun setResultPanelType(resultPanelType: ResultPanelType) = withContext(Dispatchers.IO) {
        preferences.edit().putString(RESULT_PANEL_TYPE_KEY, resultPanelType.name).apply()
    }

    override fun setResultAccuracy(accuracy: Int) {
        Log.d("setResAccuracy", accuracy.toString())
        preferences.edit().putInt(RESULT_ACCURACY_KEY, accuracy).apply()
    }

    override fun getResultAccuracy(): Int {
       return preferences.getInt(RESULT_ACCURACY_KEY, 0)
    }

    companion object {
        private const val RESULT_PANEL_TYPE_KEY = "RESULT_PANEL_TYPE_KEY"
        private const val RESULT_ACCURACY_KEY = "RESULT_ACCURACY_KEY"
    }
}
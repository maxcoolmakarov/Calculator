package com.example.calculator.domain

import com.example.calculator.ResultPanelType

interface SettingsDao {

    /**
     * getting the type of panel drawing result
     */
    suspend fun getResultPanelType(): ResultPanelType // suspend means that function may take a long time to complete

    /**
     * setting the type of panel drawing result
     */
    suspend fun setResultPanelType(resultPanelType: ResultPanelType)
}
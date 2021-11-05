package com.example.calculator.domain

import com.example.calculator.presentation.history.HistoryItem

interface HistoryRepository {

    suspend fun add(historyItem: HistoryItem)

    suspend fun getAll(): List<HistoryItem>

    suspend fun deleteAll()

}
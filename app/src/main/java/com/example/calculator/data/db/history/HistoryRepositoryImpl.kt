package com.example.calculator.data.db.history

import com.example.calculator.domain.HistoryRepository
import com.example.calculator.presentation.history.HistoryItem

class HistoryRepositoryImpl(
    private val historyItemDao: HistoryItemDao
) : HistoryRepository {
    override suspend fun add(historyItem: HistoryItem) {
        historyItemDao.insert(historyItem.toHistoryItemEntity())
    }

    override suspend fun getAll(): List<HistoryItem> =
        historyItemDao.getAll().map { it.toHistoryItem() }

    override suspend fun deleteAll() {
        historyItemDao.deleteAll()
    }


    private fun HistoryItem.toHistoryItemEntity() = HistoryItemEntity (
        id = 0,
        expression = expression,
        result = result
    )

    private fun HistoryItemEntity.toHistoryItem() = HistoryItem (
        expression = expression,
        result = result
    )

}
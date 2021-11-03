package com.example.calculator.presentation.history

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.MainViewModel
import com.example.calculator.R
import com.example.calculator.databinding.HistoryActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider

class HistoryActivity : AppCompatActivity() {

    private val viewBinding by viewBinding(HistoryActivityBinding::bind)
    private val viewModel by viewModels<HistoryViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HistoryViewModel(HistoryRepositoryProvider.get(this@HistoryActivity)) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)

        val historyAdapter = HistoryAdapter(onItemClicked = {
            viewModel.onItemClicked(it)
        })


        with(viewBinding.list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = historyAdapter
        }

        viewBinding.historyBack.setOnClickListener {
            finish()
        }

        viewModel.historyItemsState.observe(this) {
            historyAdapter.setData(it)
        }

        viewModel.closeWithResult.observe(this) {
            setResult(RESULT_OK, Intent().putExtra(HISTORY_ACTIVITY_KEY, it))
            finish()
        }


    }

    companion object {
        const val HISTORY_ACTIVITY_KEY = "HISTORY_ACTIVITY_KEY"
    }



}
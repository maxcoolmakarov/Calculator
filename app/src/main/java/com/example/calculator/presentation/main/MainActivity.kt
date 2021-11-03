package com.example.calculator.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.*
import com.example.calculator.data.db.history.HistoryResult
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.presentation.history.HistoryActivity
import com.example.calculator.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private val tag: String = this.javaClass.simpleName
    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(SettingsDaoProvider.getDao(this@MainActivity),
                HistoryRepositoryProvider.get(this@MainActivity)
                ) as T
            }
        }
    }
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    private val resultLauncher = registerForActivityResult(HistoryResult()) { item ->
        viewModel.onHistoryResult(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "OnCreate")
        setContentView(R.layout.activity_main)

        viewBinding.mainActivitySettings.setOnClickListener{
            openSettings()
        }

        viewBinding.mainHistory.setOnClickListener {
            openHistory()
        }

        viewBinding.enterField.apply {
            showSoftInputOnFocus = false
        }

        listOf(
            viewBinding.zero,
            viewBinding.one,
            viewBinding.two,
            viewBinding.three,
            viewBinding.four,
            viewBinding.five,
            viewBinding.six,
            viewBinding.seven,
            viewBinding.eight,
            viewBinding.nine).forEachIndexed{ index, textView ->
            textView.setOnClickListener{ viewModel.onNumberClick(index, viewBinding.enterField.selectionStart)}
        }

        mapOf(viewBinding.plus to Operator.PLUS,
            viewBinding.minus to Operator.MINUS,
            viewBinding.divide to Operator.DEVIDE,
            viewBinding.multiply to Operator.MULTIPLY,
            viewBinding.coma to Operator.DOT
        ).forEach { (button, operator) ->
            button.setOnClickListener { viewModel.onOperatorClick(operator, viewBinding.enterField.selectionStart) }
        }

        viewBinding.getResult.setOnClickListener { viewModel.onResult() }
        viewBinding.clearChar.setOnClickListener { viewModel.onClearChar(viewBinding.enterField.selectionStart) }
        viewBinding.clear.setOnClickListener { viewModel.onClear()}

         viewModel.expressionState.observe(this){ state ->
             viewBinding.enterField.setText(state.expression)
             viewBinding.enterField.setSelection(state.selection)
         }

        viewModel.resultState.observe(this){ state ->
            viewBinding.result.text = state
        }

        viewModel.resultPanelState.observe(this){
            with(viewBinding.result) {
                gravity = when(it){
                    ResultPanelType.LEFT -> Gravity.START.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.RIGHT -> Gravity.END.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.HIDE -> gravity
                }
                isVisible = it != ResultPanelType.HIDE
            }
        }

        viewModel.resultAccuracy.observe(this){
            viewModel.update()
        }



    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openHistory() {
        resultLauncher.launch()
//        val intent = Intent(this, HistoryActivity::class.java)
//        startActivity(intent)
    }

}


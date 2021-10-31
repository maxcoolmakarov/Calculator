package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.di.SettingsDaoProvider

class MainActivity : AppCompatActivity() {
    private val tag: String = this.javaClass.simpleName
    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(SettingsDaoProvider.getDao(this@MainActivity)) as T
            }
        }
    }
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "OnCreate")
        setContentView(R.layout.activity_main)

        viewBinding.mainActivitySettings.setOnClickListener{
            openSettings()
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
            viewBinding.coma to Operator.DOT).forEach { (button, operator) ->
            button.setOnClickListener { viewModel.onOperatorClick(operator, viewBinding.enterField.selectionStart) }
        }

        viewBinding.getResult.setOnClickListener { viewModel.onResult() }
        viewBinding.clearChar.setOnClickListener { viewModel.onClearChar(viewBinding.enterField.selectionStart) }
        viewBinding.clear.setOnClickListener { viewModel.onClear()}

         viewModel.expressionState.observe(this){ state ->
             viewBinding.enterField.setText(state.expression)
             viewBinding.enterField.setSelection(state.selection)
//             viewBinding.result.text = state
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



    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
    private fun openSettings(){
//        Toast.makeText(this, "Open Settings", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}


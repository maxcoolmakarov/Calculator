package com.example.calculator.presentation.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.R
import com.example.calculator.ResultPanelType
import com.example.calculator.SettingsViewModel
import com.example.calculator.databinding.SettingsactivityBinding
import com.example.calculator.di.SettingsDaoProvider

class SettingsActivity : AppCompatActivity(){

    private val viewBinding by viewBinding(SettingsactivityBinding::bind)
    private val viewModel by viewModels<SettingsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SettingsViewModel(SettingsDaoProvider.getDao(this@SettingsActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settingsactivity)
        viewBinding.settingsBack.setOnClickListener {
            finish()
        }

        viewBinding.resultPanelConteiner.setOnClickListener {
            viewModel.openResultPanelAction()
        }

        viewModel.resultPanelState.observe(this) { state ->
            viewBinding.resultPanelDescription.text = resources.getStringArray(R.array.result_panel_tpes)[state.ordinal]
        }

        viewModel.openResultPanelAction.observe(this) { type ->
            showDialog(type)
        }

    }

    private fun showDialog(type: ResultPanelType) {
        var result: Int? = null
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.settings_result_panel))
            .setPositiveButton("Ok") { dialog, id ->
                result?.let { viewModel.onResultPanelStateChanged(ResultPanelType.values()[it]) }
            }
            .setNegativeButton("Cancel") { dialog, id ->

            }
            .setSingleChoiceItems(R.array.result_panel_tpes, type.ordinal) { dialog, id ->
                result = id
            }
            .show()
    }


}
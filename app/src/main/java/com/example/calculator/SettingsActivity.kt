package com.example.calculator

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.databinding.SettingsactivityBinding

class SettingsActivity : AppCompatActivity(){

    private val viewBinding by viewBinding(SettingsactivityBinding::bind)
    private val viewModel by viewModels<SettingsViewModel>()

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
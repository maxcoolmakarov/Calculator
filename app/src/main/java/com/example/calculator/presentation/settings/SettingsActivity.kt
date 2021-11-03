package com.example.calculator.presentation.settings

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.SeekBar
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

        viewBinding.accuracy.progress = viewModel.resultAccuracy.value?: 0
        if (viewBinding.accuracy.progress != 0)
            viewBinding.accuracyLabel.setText("Accuracy: " + viewBinding.accuracy.progress.toString())
        else
            viewBinding.accuracyLabel.setText("Accuracy: Default")

        viewBinding.settingsBack.setOnClickListener {
            finish()
        }

        viewBinding.resultPanelConteiner.setOnClickListener {
            viewModel.openResultPanelAction()
        }

        viewBinding.accuracy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.onAccuracyBarChanged(progress)
                    if (progress != 0)
                        viewBinding.accuracyLabel.setText("Accuracy: " + progress.toString())
                    else
                        viewBinding.accuracyLabel.setText("Accuracy: Default")
                }

                Log.d("Settings", "polzunok"+progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

//        viewModel.resultAccuracy.observe(this) { state ->
//            viewBinding.accuracy.progress = state
//        }

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
package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val tag: String = this.javaClass.simpleName
    private val viewModel: MainViewModel by viewModels()
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "OnCreate")
        setContentView(R.layout.activity_main)

        viewBinding.but.setOnClickListener{
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
            textView.setOnClickListener{ viewModel.onNumberClick(index)}
        }

                 viewModel.expressionState.observe(this){ state ->
                     viewBinding.enterField.setText(state)
                     viewBinding.result.setText(state)
                 }

    }

    override fun onStart() {
        super.onStart()
        Log.d(tag,"OnStart")
    }
    private fun openSettings(){
        Toast.makeText(this, "Open Settings", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}
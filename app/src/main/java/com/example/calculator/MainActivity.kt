package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val tag: String = this.javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "OnCreate")
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.one)
        button.setOnClickListener{
            openSettings()
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
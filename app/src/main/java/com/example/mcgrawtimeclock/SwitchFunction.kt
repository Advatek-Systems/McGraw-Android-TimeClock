package com.example.mcgrawtimeclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mcgrawtimeclock.databinding.ActivitySwitchFunctionBinding

class SwitchFunction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySwitchFunctionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
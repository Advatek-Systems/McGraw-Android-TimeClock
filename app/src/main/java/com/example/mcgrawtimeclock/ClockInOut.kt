package com.example.mcgrawtimeclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mcgrawtimeclock.common.Utils
import com.example.mcgrawtimeclock.databinding.ActivityClockInOutBinding

class ClockInOut : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityClockInOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Utils.setupActivityUI(this, binding)
    }
}
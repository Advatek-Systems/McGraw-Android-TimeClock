package com.example.mcgrawtimeclock

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.mcgrawtimeclock.common.Utils
import com.example.mcgrawtimeclock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Utils.setupActivityUI(this, binding)

        binding.txtScanCard.requestFocus()

        binding.txtScanCard.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.isNullOrBlank()) {
                Utils.showErrorBox(applicationContext, "ID Card Required!", "ID Card is required and cannot be left blank!")
                binding.txtScanCard.requestFocus()
                return@addTextChangedListener
            }

            //redirect to new activity
        }
    }
}
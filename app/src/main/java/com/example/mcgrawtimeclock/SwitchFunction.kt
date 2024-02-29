package com.example.mcgrawtimeclock

import android.content.Context
import com.example.mcgrawtimeclock.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.mcgrawtimeclock.common.Utils
import com.example.mcgrawtimeclock.databinding.ActivitySwitchFunctionBinding

class SwitchFunction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySwitchFunctionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Utils.setupActivityUI(this, binding)

        val cardNo = intent.getIntExtra("cardNumber", -1)
        if (cardNo == -1) { finish() }

        val functionList = functionDao.getAllFunctions()

        val functionDescriptions = functionList.map { "${it.functionNo} - ${it.description}" }

        val adapter = ArrayAdapter(this, R.layout.spinner_item, functionDescriptions)
        binding.spnFunctions.adapter = adapter

        var isFirstSelection = true

        binding.spnFunctions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }

                val selectedFunctionNo = functionList[position].functionNo
                transDao.insertTransRecord(cardNo, Utils.getCurrentDateAsInt(), Utils.getCurrentTimeAsInt(), "IN", selectedFunctionNo.toString(), Utils.getLocalIpAddress())
                Utils.showSuccessBox(this@SwitchFunction, "Success!", "You have been clocked IN!") {
                    finishToMain = true
                    finish()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
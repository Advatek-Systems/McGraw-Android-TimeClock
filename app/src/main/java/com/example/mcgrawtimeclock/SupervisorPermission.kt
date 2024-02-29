package com.example.mcgrawtimeclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.mcgrawtimeclock.common.Utils
import com.example.mcgrawtimeclock.databinding.ActivitySupervisorPermissionBinding

class SupervisorPermission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySupervisorPermissionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Utils.setupActivityUI(this, binding)

        val cardNo = intent.getIntExtra("cardNumber", -1)
        if (cardNo == -1) { finish() }

        binding.txtSupervisorCard.requestFocus()

        binding.txtSupervisorCard.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                var text = binding.txtSupervisorCard.text.toString()
                if (text.isNullOrEmpty() || text.isNullOrBlank()) {
                    Utils.showErrorBox(this, "ID Card Required!", "ID Card is required and cannot be left blank!")
                    binding.txtSupervisorCard.setText("")
                    binding.txtSupervisorCard.requestFocus()
                    return@OnKeyListener true
                }

                if (text.length == 9) {
                    var index = text.indexOf(':')
                    var input = text.substring(index + 1, text.length)
                    var intInput = input.toInt()
                    if (employeeDao.isEmployeeSupervisor(intInput) == 0) {
                        Utils.showErrorBox(this, "Error", "Invalid card number, supervisor permission is required! Please try again!") {
                            finish()
                        }
                        return@OnKeyListener true
                    } else {
                        binding.txtSupervisorCard.setText("")
                        binding.txtSupervisorCard.requestFocus()
                        val intent = Intent(this, SwitchFunction::class.java)
                        intent.putExtra("cardNumber", cardNo)
                        startActivity(intent)
                    }
                }
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onResume() {
        super.onResume()
        if (finishToMain)
            finish()
    }
}
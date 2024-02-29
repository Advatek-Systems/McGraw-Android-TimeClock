package com.example.mcgrawtimeclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.mcgrawtimeclock.common.Utils
import com.example.mcgrawtimeclock.databinding.ActivityClockInOutBinding
import java.net.NetworkInterface
import java.net.SocketException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ClockInOut : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityClockInOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Utils.setupActivityUI(this, binding)

        val cardNo = intent.getIntExtra("cardNumber", -1)
        val functionNo = employeeDao.getEmployeeRegDept(cardNo)
        if (cardNo == -1 || functionNo == -1) { finish() }

        val emplName = employeeDao.getEmployeeName(cardNo)
        binding.txtWelcomeText.text = "Welcome, $emplName"

        binding.btnClockIn.setOnClickListener {
            transDao.insertTransRecord(cardNo, Utils.getCurrentDateAsInt(), Utils.getCurrentTimeAsInt(), "IN", functionNo.toString(), Utils.getLocalIpAddress())
            Utils.showSuccessBox(this, "Success!", "You have been clocked IN!") {
                finish()
            }
        }

        binding.btnClockOut.setOnClickListener {
            transDao.insertTransRecord(cardNo, Utils.getCurrentDateAsInt(), Utils.getCurrentTimeAsInt(), "OUT", functionNo.toString(), Utils.getLocalIpAddress())
            Utils.showSuccessBox(this, "Success!", "You have been clocked OUT!") {
                finish()
            }
        }

        binding.btnSwitch.setOnClickListener {
            val intent = Intent(this, SwitchFunction::class.java)
            intent.putExtra("cardNumber", cardNo)
            startActivity(intent)
        }
    }
}
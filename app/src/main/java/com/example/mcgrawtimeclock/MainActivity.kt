package com.example.mcgrawtimeclock

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.mcgrawtimeclock.common.Utils
import com.example.mcgrawtimeclock.dao.EmployeeDAO
import com.example.mcgrawtimeclock.dao.FunctionDAO
import com.example.mcgrawtimeclock.dao.TransDAO
import com.example.mcgrawtimeclock.database.EmployeeDatabase
import com.example.mcgrawtimeclock.database.FunctionDatabase
import com.example.mcgrawtimeclock.database.TransDatabase
import com.example.mcgrawtimeclock.databinding.ActivityMainBinding
import com.example.mcgrawtimeclock.service.SyncService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

lateinit var employeeDao: EmployeeDAO
lateinit var functionDao: FunctionDAO
lateinit var transDao: TransDAO

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var employeeDB: EmployeeDatabase
        var functionDB: FunctionDatabase
        var transDB: TransDatabase

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startService(Intent(this, SyncService::class.java))

        Utils.setupActivityUI(this, binding)
        Toast.makeText(this, "Instantiating Databases, Please Wait a Moment", Toast.LENGTH_LONG).show()
        MainScope().launch {
            withContext(Dispatchers.Default) {
                employeeDB = instantiateEmployeeDatabase()
                employeeDao = employeeDB.employeeDao()

                functionDB = instantiateFunctionDatabase()
                functionDao = functionDB.functionDao()

                transDB = instantiateTransDatabase()
                transDao = transDB.transDao()
            }
            Toast.makeText(applicationContext, "Databases Successfully Instantiated", Toast.LENGTH_LONG).show()
        }

        binding.txtScanCard.requestFocus()

        var isTextCleared = false
        binding.txtScanCard.addTextChangedListener { text ->
            if (!isTextCleared) {
                if (text.isNullOrEmpty() || text.isNullOrBlank()) {
                    Utils.showErrorBox(this, "ID Card Required!", "ID Card is required and cannot be left blank!")
                    isTextCleared = true
                    binding.txtScanCard.setText("")
                    binding.txtScanCard.requestFocus()
                    isTextCleared = false
                    return@addTextChangedListener
                }

                if (text.length == 9) {
                    var index = text.indexOf(':')
                    var input = text.substring(index + 1, text.length)
                    var intInput = input.toInt()
                    if (employeeDao.validateCardNo(intInput) == 0) {
                        Utils.showErrorBox(this, "Error", "Invalid card number, please try again!")
                        isTextCleared = true
                        binding.txtScanCard.setText("")
                        binding.txtScanCard.requestFocus()
                        isTextCleared = false
                        return@addTextChangedListener
                    } else {
                        isTextCleared = true
                        binding.txtScanCard.setText("")
                        binding.txtScanCard.requestFocus()
                        isTextCleared = false
                        val intent = Intent(this, ClockInOut::class.java)
                        intent.putExtra("cardNumber", intInput)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun instantiateEmployeeDatabase(): EmployeeDatabase{
        return Room.databaseBuilder(
            applicationContext,
            EmployeeDatabase::class.java, "employee"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    private fun instantiateFunctionDatabase(): FunctionDatabase{
        return Room.databaseBuilder(
            applicationContext,
            FunctionDatabase::class.java, "function"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    private fun instantiateTransDatabase(): TransDatabase{
        return Room.databaseBuilder(
            applicationContext,
            TransDatabase::class.java, "trans"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }
}
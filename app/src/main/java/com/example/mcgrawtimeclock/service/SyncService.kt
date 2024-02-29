package com.example.mcgrawtimeclock.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.mcgrawtimeclock.api.ApiService
import com.example.mcgrawtimeclock.employeeDao
import com.example.mcgrawtimeclock.functionDao
import com.example.mcgrawtimeclock.transDao
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class SyncService : Service() {
    private val TAG: String = "SyncService"
    private lateinit var timer: Timer
    private val handler: Handler = Handler()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        logError("Service Started")
        timer = Timer()
        startSyncTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        logError("Service Stopped")
        timer.cancel()
    }

    private fun startSyncTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    syncDataIfConnected()
                }
            }
        }, 0, SYNC_INTERVAL)

    }

    private fun syncDataIfConnected() {
        if (isConnectedToInternet()) {
            MainScope().launch {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.2.3/McGrawAPI/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(ApiService::class.java)

                    val employeeResponse = apiService.getEmployees()
                    if (employeeResponse.isSuccessful && employeeResponse.code() == 200) {
                        val employeeList = employeeResponse.body()
                        if (!employeeList.isNullOrEmpty()) {
                            employeeList.forEach {
                                if (employeeDao.doesEmployeeExist(it.emplNo) == 0) {
                                    employeeDao.insertEmployee(it.emplNo, it.card, it.firstName, it.lastName, it.regDept, it.supervisor, it.category)
                                    logError("Employee inserted")
                                } else {
                                    employeeDao.updateEmployee(it.emplNo, it.card, it.firstName, it.lastName, it.regDept, it.supervisor, it.category)
                                    logError("Employee updated")
                                }
                            }
                        } else {
                            logError("101 - Null or empty response body")
                        }
                    } else {
                        logError("101 - API error (non 200 status)")
                    }

                    val functionResponse = apiService.getFunctions()
                    if (functionResponse.isSuccessful && functionResponse.code() == 200) {
                        val functionList = functionResponse.body()
                        if (!functionList.isNullOrEmpty()) {
                            functionList.forEach {
                                if (functionDao.doesFunctionExist(it.functionNo) == 0) {
                                    functionDao.insertFunction(it.functionNo, it.description, it.enabled, it.departmentNo)
                                    logError("Function inserted")
                                } else {
                                    functionDao.updateFunction(it.functionNo, it.description, it.enabled, it.departmentNo)
                                    logError("Function updated")
                                }
                            }
                        } else {
                            logError("102 - Null or empty response body")
                        }
                    } else {
                        logError("102 - API error (non 200 status)")
                    }

                    val transactions = transDao.getUnsentTransRecords()
                    transactions.forEach{
                        val call = apiService.insertTrans(it)

                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    transDao.updateSentStatus(it.transID)
                                    logError("Transaction Inserted")
                                } else {
                                    logError("103 - ${it.transID} - (non 200 status)")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                logError("103 - Transaction Upload Error")
                            }
                        })
                    }
                } catch (e: Exception) {
                    logError("Error Fell into catch: " + e.message)
                }
            }
        }
    }

    private fun logError(message: String) {
        Log.e(TAG, message)
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    companion object {
        private const val SYNC_INTERVAL: Long = 60000 // 1 minutes in milliseconds
    }
}

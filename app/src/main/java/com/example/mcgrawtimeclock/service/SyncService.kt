package com.example.mcgrawtimeclock.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mcgrawtimeclock.MainActivity
import com.example.mcgrawtimeclock.R
import com.example.mcgrawtimeclock.api.ApiService
import com.example.mcgrawtimeclock.employeeDao
import com.example.mcgrawtimeclock.functionDao
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
        Log.d(TAG, "Service onCreate")
        timer = Timer()
        startForegroundService()
        startSyncTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service onDestroy")
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
                        .baseUrl("http://10.1.5.200:45455/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(ApiService::class.java)

                    val employeeResponse = apiService.getEmployees()
                    if (employeeResponse.isSuccessful && employeeResponse.code() == 200) {
                        val employeeList = employeeResponse.body()
                        if (!employeeList.isNullOrEmpty()) {
                            employeeList.forEach {
                                if (employeeDao.doesEmployeeExist(it.emplNo) == 0) {
                                    employeeDao.insertEmployee(it.emplNo, it.card, it.firstName, it.lastName, it.regDept, it.supervisor)
                                    logError("Employee inserted")
                                } else {
                                    employeeDao.updateEmployee(it.emplNo, it.card, it.firstName, it.lastName, it.regDept, it.supervisor)
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
                } catch (e: Exception) {
                    logError("Error Fell into catch: " + e.message)
                }
            }
        } else {
            Log.d(TAG, "No internet connectivity")
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

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("sync_service", "Sync Service")
            } else {
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Sync Service")
            .setContentText("Running...")
            .setSmallIcon(R.drawable.advateksystemstriangle)
            .setContentIntent(getPendingIntent())
            .build()

        startForeground(NOTIFICATION_ID, notificationBuilder)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            return channelId
        }
        return ""
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        private const val SYNC_INTERVAL: Long = 60000 // 1 minute in milliseconds
        private const val NOTIFICATION_ID = 1
    }
}
package com.example.mcgrawtimeclock.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.IBinder
import java.util.*

class SyncService : Service() {
    private lateinit var timer: Timer
    private val handler: Handler = Handler()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        timer = Timer()
        startSyncTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
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
            //TODO Retrofit stuff in here
        }
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

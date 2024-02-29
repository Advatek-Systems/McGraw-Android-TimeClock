package com.example.mcgrawtimeclock.common

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.mcgrawtimeclock.databinding.ActivityMainBinding
import java.net.NetworkInterface
import java.net.SocketException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Utils {
    fun showSuccessBox(context: Context, title: String, message: String, callback: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)

        builder.setMessage(message)

        val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
        toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 1000)

        val alertDialog = builder.create()
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            alertDialog.dismiss()
            callback?.invoke()
        }, 500)
    }

    fun showErrorBox(context: Context, title: String, message: String, callback: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)

        builder.setMessage(message)

        val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGen.startTone(ToneGenerator.TONE_SUP_ERROR, 1000)
        toneGen.startTone(ToneGenerator.TONE_SUP_BUSY, 1000)

        val alertDialog = builder.create()
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            alertDialog.dismiss()
            callback?.invoke()
        }, 5000)
    }

    fun <T : ViewBinding> setupActivityUI(activity: AppCompatActivity, binding: T) {
        activity.window.decorView.apply {
            systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
        activity.supportActionBar?.hide()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    fun getCurrentDateAsInt(): Int {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyMMdd")
        return currentDate.format(formatter).toInt()
    }

    fun getCurrentTimeAsInt(): Int {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HHmmss")
        return currentTime.format(formatter).toInt()
    }

    fun getLocalIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address.hostAddress.contains(":").not()) {
                        return address.hostAddress
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return ""
    }
}
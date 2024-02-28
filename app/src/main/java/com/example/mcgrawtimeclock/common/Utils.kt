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

object Utils {
    fun showErrorBox(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)

        builder.setMessage(message)

        //builder.setPositiveButton(android.R.string.ok) { _, _ ->
        //}

        val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneGen.startTone(ToneGenerator.TONE_SUP_ERROR, 500)
        toneGen.startTone(ToneGenerator.TONE_SUP_BUSY, 500)

        val alertDialog = builder.create()
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            alertDialog.dismiss()
        }, 3000)
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
}
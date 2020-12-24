package com.submission.themoviedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.submission.themoviedb.helper.barSetup

class SplashScreenActivity : AppCompatActivity() {

    private val delay: Long = 3200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        barSetup(window, applicationContext, true)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
            }, delay
        )
    }
}
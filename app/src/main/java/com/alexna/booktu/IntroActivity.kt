package com.alexna.booktu

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(300)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
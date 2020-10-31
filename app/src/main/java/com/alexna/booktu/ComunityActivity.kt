package com.alexna.booktu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ComunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comunity)
    }

    fun goDiscussionActivity(view: View) {
        val intent = Intent(this@ComunityActivity, DiscussionActivity::class.java)
        startActivity(intent)
    }

    fun goMainActivity(view: View) {
        val intent = Intent(this@ComunityActivity, MainActivity::class.java)
        startActivity(intent)
    }


}
package com.alexna.booktu.accountActivity

import android.app.Application
import com.alexna.booktu.dataAdpater.PreferenceUtil

class MySharedPreferences:Application() {

    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}


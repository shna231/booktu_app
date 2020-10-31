package com.alexna.booktu.accountActivity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alexna.booktu.MainActivity
import com.alexna.booktu.R
import kotlinx.android.synthetic.main.activity_my_info.*

class MyInfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        logout.setOnClickListener {

            val intent_main = Intent(this@MyInfoActivity, MainActivity::class.java)

            MySharedPreferences.prefs.setString("id", "")
            MySharedPreferences.prefs.setString("nickname", "")
            MySharedPreferences.prefs.setString("email", "")
            MySharedPreferences.prefs.setString("logined", "no")

            var dialog = AlertDialog.Builder(this@MyInfoActivity)
                .setTitle("알림")
                .setMessage("로그아웃되었습니다. 메인으로 이동합니다.")
                .setNeutralButton("확인", DialogInterface.OnClickListener(){ dialog, which -> startActivity(intent_main)})
                .show()
        }
    }

}
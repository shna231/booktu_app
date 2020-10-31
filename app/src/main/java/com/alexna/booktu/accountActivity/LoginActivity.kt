package com.alexna.booktu.accountActivity

import Member
import MemberItem
import MemberLoginAPI
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alexna.booktu.MainActivity
import com.alexna.booktu.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    // response 바디를 넣을 ResponseGet 클래스 생성
    var loginjson:Member? = null

    // gson 객체 생성
    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    // 레트로핏 객체 생성
    var retrofit =
        Retrofit.Builder()
            .baseUrl("https://booktu.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    // 레트로핏 객체로 인터페이스 생성
    var server = retrofit.create(MemberLoginAPI::class.java)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 검증 시작: 공백 검사 -> 정보 검사 -> 로그인 후 메인화면
        login.setOnClickListener {

            // 1. Login.xml의 Edit Text 값 가져옴.
            val id = id.text.toString()
            val password = password.text.toString()

            // 2. 두 입력값이 공백이면 알림 메시지 출력
            if (id.isEmpty() || password.isEmpty()) {
                var dialog = AlertDialog.Builder(this@LoginActivity)
                    .setTitle("알림")
                    .setMessage("정보를 모두 입력해주세요.")
                    .setNeutralButton("확인", null)
                    .show()
            }

            // 3. 로그인
            else {

                server.getUserInfo(id, password)
                    .enqueue(object : Callback<MemberItem>{
                        override fun onFailure(call: Call<MemberItem>, t: Throwable) {

                            Log.e("로그인 실패", "통신 오류 발생 "+ t?.toString())

                            var dialog = AlertDialog.Builder(this@LoginActivity)
                                .setTitle("알림")
                                .setMessage("로그인에 실패하였습니다. 다시 시도해주세요.")
                                .setNeutralButton("확인", null)
                                .show()

                        } override fun onResponse(call: Call<MemberItem>, response: Response<MemberItem>) {

                            val res = response.body()
                            loginjson =  res?.MemberDatalist?.get(0)

                            MySharedPreferences.prefs.setString("id", loginjson!!.id)
                            MySharedPreferences.prefs.setString("nickname", loginjson!!.nickname)
                            MySharedPreferences.prefs.setString("email", loginjson!!.email)
                            MySharedPreferences.prefs.setString("logined", "yes")


                            val intent_main = Intent(this@LoginActivity, MainActivity::class.java)

                            var dialog = AlertDialog.Builder(this@LoginActivity)
                                .setTitle("알림")
                                .setMessage("${loginjson?.nickname}님, 안녕하세요!")
                                .setNeutralButton("확인",
                                    { dialog, which -> startActivity(intent_main)})
                                .show()

                        }

                    })


            }
        }

        // 회원가입
        signup.setOnClickListener {
            val intent_signup = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent_signup)
        }


        find.setOnClickListener {

        }



    }



}


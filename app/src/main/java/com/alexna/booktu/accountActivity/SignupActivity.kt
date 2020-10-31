package com.alexna.booktu.accountActivity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alexna.booktu.R
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup.setOnClickListener {

            val intent_login = Intent(this@SignupActivity, LoginActivity::class.java)

            val id = id.text.toString()
            val password = password.text.toString()
            val nickname = nickname.text.toString()
            val email = email.text.toString()

            // 1. 해당 id가 존재하는지 DB에 검색
            // val task_find = FindData()
            // task_find.execute("https://booktu.azurewebsites.net/????", id)

            // 2. 중복 확인 완료 후 데이터 삽입
            val task_insert = InsertData()
            task_insert.execute(
                "https://booktu.azurewebsites.net/appinsert.php",
                id,
                password,
                nickname,
                email
            )

            // 3. 완료 알림 출력 및 로그인 화면으로 전환
            startActivity(intent_login)

            // signup(id, password, nickname, email)
        }
    }

    private class InsertData : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {

            val serverURL: String? = params[0]
            val id: String? = params[1]
            val password: String? = params[2]
            val nickname:String? = params[3]
            val email:String? = params[4]

            val postParameters: String = "id=$id&password=$password&nickname=$nickname&email=$email"

            try {
                val url = URL(serverURL)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection


                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()


                val outputStream: OutputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode: Int = httpURLConnection.responseCode


                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }


                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                val sb = StringBuilder()
                var line: String? = null

                while (bufferedReader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }

                bufferedReader.close();

                return sb.toString();

            } catch (e: Exception) {
                return "Error" + e.message
            }
        }

    }
}

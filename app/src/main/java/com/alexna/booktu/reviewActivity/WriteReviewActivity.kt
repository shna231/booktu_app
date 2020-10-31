package com.alexna.booktu.reviewActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alexna.booktu.R
import com.alexna.booktu.accountActivity.MySharedPreferences
import com.alexna.booktu.dataAdpater.BookReviewInsertAPI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_write_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WriteReviewActivity : AppCompatActivity() {

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
    var server = retrofit.create(BookReviewInsertAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)


        book_search_bar.setOnClickListener {
            startActivity(Intent(this, SelectBookActivity::class.java))
        }

        // 책 정보 표시 - title, author, publisher, image
        /*------------------------------------------------------------------------------------------*/
        if(intent.hasExtra("image")) {
            Glide.with(this).load(intent.getStringExtra(("image")))
                .apply(RequestOptions().override(300, 450))
                .apply(RequestOptions.centerCropTransform())
                .into(book_image)
        }

        if(intent.hasExtra("title")) {
            val tr1 = "<b>"
            val tr2 = "</b>"
            val realTitle = intent.getStringExtra("title")!!.replace(tr1, "").replace(tr2, "")
            book_title.setText(realTitle)
        }
        else {
            var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                .setTitle("알림")
                .setMessage("독후감으로 쓸 책을 등록해주세요.")
                .setNeutralButton("확인", null)
                .show()
        }

        if(intent.hasExtra("author")) {
            book_author.setText("${intent.getStringExtra("author")}")
        }
        if(intent.hasExtra("publisher")) {
            show_publisher.setText("${intent.getStringExtra("publisher")}")
        }
        /*------------------------------------------------------------------------------------------*/



        accept.setOnClickListener{

            // 1. SQL 데이터 삽입용 변수 선언 - writer_id, isbn, title, content (date, read_count, open, comment 보류)
            /*------------------------------------------------------------------------------------------*/
            val writer_id = MySharedPreferences.prefs.getString("id", "")
            val title = review_title.text.toString()
            val content = review_content.text.toString()

            var book_title =""
            if (intent.hasExtra("title")) {
                book_title = intent.getStringExtra("title").toString()
            }
            else {
                Log.e("독후감 등록 실패", "intent - 'title'이 존재하지 않음")
            }

            var book_image =""
            if (intent.hasExtra("image")) {
                book_image = intent.getStringExtra("image").toString()
            }
            else {
                Log.e("독후감 등록 실패", "intent - 'image'가 존재하지 않음")
            }

            var book_publisher=""
            if (intent.hasExtra("publisher")) {
                book_publisher = intent.getStringExtra("publisher").toString()
            }
            else {
            Log.e("독후감 등록 실패", "intent - 'publisher'가 존재하지 않음")
            }

            var book_author=""
            if (intent.hasExtra("author")) {
                book_author = intent.getStringExtra("author").toString()
            }
            else {
                Log.e("독후감 등록 실패", "intent - 'author'가 존재하지 않음")
            }



            /*------------------------------------------------------------------------------------------*/


            // 2. 독후감 제목 및 내용 공백 체크
            /*------------------------------------------------------------------------------------------*/
            if (title.isEmpty() || content.isEmpty()) {
                var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                    .setTitle("알림")
                    .setMessage("제목과 내용을 모두 입력해주세요.")
                    .setNeutralButton("확인", null)
                    .show()
            }
            /*------------------------------------------------------------------------------------------*/

            server.insertReview(writer_id, title, content, book_title, book_image, book_publisher, book_author)
                .enqueue(object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("레트로핏 - 독후감 삽입 실패", "시발 오류 발생" + t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("레트로핏 - 독후감 삽입 성공", "왜되지")
                    }
                })


            // 3. 책 및 독후감 정보 삽입
            /*------------------------------------------------------------------------------------------
            val intent_main = Intent(this@WriteReviewActivity, MainActivity::class.java)

            val task_insert = InsertReviewData()
            task_insert.execute(
                "https://booktu.azurewebsites.net/reviewinsert.php",
                writer_id,
                title,
                content,
                book_title,
                book_image,
                book_publisher,
                book_author
            )

            var dialog = AlertDialog.Builder(this@WriteReviewActivity)
                .setTitle("알림")
                .setMessage("독후감을 작성하였습니다.")
                .setNeutralButton("확인",
                    { dialog, which -> startActivity(intent_main)})
                .show()

            /*------------------------------------------------------------------------------------------*/

        }

    }

    private class InsertReviewData : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {

            val serverURL: String? = params[0]
            val writer_id: String? = params[1]
            val title:String? = params[2]
            val content:String? = params[3]
            val book_title:String? = params[4]
            val book_image:String? = params[5]
            val book_publisher:String? = params[6]
            val book_author:String? =params[7]

            val postParameters = "writer_id=$writer_id&title=$title&content=$content&book_title=$book_title&book_image=$book_image&book_publisher=$book_publisher&book_author=$book_author"

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
                return "독후감 등록 에러" + e.message
            }*/
        }

    }

}


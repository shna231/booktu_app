package com.alexna.booktu.reviewActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alexna.booktu.R
import com.alexna.booktu.dataAdpater.BookInfoAPI
import com.alexna.booktu.dataAdpater.BookReviews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_show_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowReviewActivity : AppCompatActivity() {

    // 책 이미지 > 책 제목 / 작가 / 출판사 가져오기
   /*------------------------------------------------------------------------------*/
    // response 받을 변수
    var bookInfo: BookReviews? = null

    // gson 객체 생성
    var gson_bookInfo: Gson = GsonBuilder()
        .setLenient()
        .create()

    // 레트로핏 객체 생성
    var retrofit_bookInfo =
        Retrofit.Builder()
            .baseUrl("https://booktu.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create(gson_bookInfo))
            .build()
    /*------------------------------------------------------------------------------*/

    // 독후감 제목 > 삭제
    /*------------------------------------------------------------------------------
    // gson 객체 생성
    var gson_reviewDelete: Gson = GsonBuilder()
        .setLenient()
        .create()

    // 레트로핏 객체 생성
    var retrofit_reviewDelete =
        Retrofit.Builder()
            .baseUrl("https://booktu.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create(gson_reviewDelete))
            .build()
    ------------------------------------------------------------------------------*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_review)



        if(intent.hasExtra("review_title") && intent.hasExtra("review_content") && intent.hasExtra("book_image")) {
            review_title.setText(intent.getStringExtra("review_title")).toString()
            review_content.setText(intent.getStringExtra("review_content")).toString()

            Glide.with(this).load(intent.getStringExtra(("book_image")))
                .apply(RequestOptions().override(300, 450))
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)

            var server = retrofit_bookInfo.create(BookInfoAPI::class.java)

            server.getBookInfo(intent!!.getStringExtra("book_image")!!)
                .enqueue(object: Callback<BookReviews> {
                    override fun onFailure(call: Call<BookReviews>, t: Throwable) {
                        Log.e("독후감 책 정보 받기 실패 ", "오류 발생 "+ t?.toString())

                    }
                    override fun onResponse(call: Call<BookReviews>, response: Response<BookReviews>) {
                        bookInfo = response.body()
                        val book = bookInfo?.items?.get(0)

                        Log.d("독후감 책 정보 받기 성공", "제목:${book?.book_title}, 작가: ${book?.book_author}, 출판사:${book?.book_publisher}")

                        show_title.text = book?.book_title
                        show_author.text = book?.book_author
                        show_publisher.text = book?.book_publisher


                    }

                })

        }

        else {
            var dialog = AlertDialog.Builder(this@ShowReviewActivity)
                .setTitle("알림")
                .setMessage("잘못된 접근입니다.")
                .setNeutralButton("확인", null)
                .show()
        }


        /*
        delete_button.setOnClickListener{

            var delete_server = retrofit_reviewDelete.create(ReviewDeleteAPI::class.java)

            delete_server.deleteReview(intent!!.getStringExtra("review_title")!!)
                .enqueue(object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("독후감 삭제 실패", "오류 발생" + t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("독후감 삭제 성공", "")
                    }
                })

        }

         */


    }


}
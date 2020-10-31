package com.alexna.booktu

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexna.booktu.accountActivity.LoginActivity
import com.alexna.booktu.accountActivity.MyInfoActivity
import com.alexna.booktu.accountActivity.MySharedPreferences
import com.alexna.booktu.dataAdpater.BookReview
import com.alexna.booktu.dataAdpater.BookReviewAPI
import com.alexna.booktu.dataAdpater.BookReviews
import com.alexna.booktu.dataAdpater.ReviewListAdaptor
import com.alexna.booktu.reviewActivity.ShowReviewActivity
import com.alexna.booktu.reviewActivity.WriteReviewActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ReviewListAdaptor
    var reviewList: BookReviews? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 독후감 리스트 보기
        /*------------------------------------------------------------------------------*/
        var mRecyclerView:RecyclerView = findViewById(R.id.bRecyclerView) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)

        var server = retrofit.create(BookReviewAPI::class.java)

        server.getReviews(MySharedPreferences.prefs.getString("id", ""))
            .enqueue(object : Callback<BookReviews> {
                override fun onFailure(call: Call<BookReviews>, t: Throwable) {
                    Log.e("독후감 리스트 받기 실패 ", "오류 발생 "+ t?.toString())
                }

                override fun onResponse(call: Call<BookReviews>, response: Response<BookReviews>) {

                    Log.d("독후감 리스트 받기 성공", "")

                    reviewList = response.body()

                    runOnUiThread{
                        adapter = ReviewListAdaptor(reviewList!!)
                        mRecyclerView.adapter = adapter
                    }

                    adapter.setItemClickListener(object: ReviewListAdaptor.ItemClickListener{
                        override fun onClick(view: View, position:Int){
                            Log.d("clicked", "${position}번")

                            // 해당 독후감 페이지로 이동
                            showClickedReview(reviewList?.items?.get(position))
                        }
                    })

                }
            })

        /*-------------------------------------------------------------------------------*/


        write_review.setOnClickListener {
            val intent_review = Intent(this@MainActivity, WriteReviewActivity::class.java)
            val intent_login = Intent(this@MainActivity, LoginActivity::class.java)


            if (MySharedPreferences.prefs.getString("logined", "").equals("yes")) {
                startActivity(intent_review)
            } else {
                var dialog = AlertDialog.Builder(this@MainActivity)
                    .setTitle("알림")
                    .setMessage("로그인이 필요한 서비스입니다.")
                    .setNegativeButton("취소", null)
                dialog.setPositiveButton(
                    "로그인",
                    { dialog, which -> startActivity(intent_login) })
                dialog.show()
            }
        }

        to_community.setOnClickListener {
            startActivity(Intent(this@MainActivity, ComunityActivity::class.java))
        }

        to_discussion.setOnClickListener {
            startActivity(Intent(this@MainActivity, DiscussionActivity::class.java))
        }

        // MyInfo Button > 로그인 시 MyInfoActivity, 로그인 상태 아니면 LoginActivity
        /*-------------------------------------------------------------------------------*/
        login.setOnClickListener {

            val intent_myinfo = Intent(this@MainActivity, MyInfoActivity::class.java)
            val intent_login = Intent(this@MainActivity, LoginActivity::class.java)


            if (MySharedPreferences.prefs.getString("logined", "").equals("yes")) {
                startActivity(intent_myinfo)
            } else {
                var dialog = AlertDialog.Builder(this@MainActivity)
                    .setTitle("알림")
                    .setMessage("로그인이 필요한 서비스입니다.")
                    .setNegativeButton("취소", null)
                dialog.setPositiveButton(
                    "로그인",
                    DialogInterface.OnClickListener() { dialog, which -> startActivity(intent_login) })
                dialog.show()
            }
        }

        /*-------------------------------------------------------------------------------*/



    }

    fun showClickedReview(review: BookReview?) {
        val Intent = Intent(this, ShowReviewActivity::class.java)
        Intent.putExtra("review_title", "${review?.title}")
        Intent.putExtra("review_content", "${review?.content}")
        Intent.putExtra("book_image", "${review?.book_image}")

        startActivity(Intent)
    }


}

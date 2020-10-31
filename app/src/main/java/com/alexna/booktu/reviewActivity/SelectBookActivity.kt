package com.alexna.booktu.reviewActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexna.booktu.R
import com.alexna.booktu.dataAdpater.BookResult
import com.alexna.booktu.dataAdpater.BookSearchAPI
import com.alexna.booktu.dataAdpater.BookSearchAdaptor
import com.alexna.booktu.dataAdpater.Items
import kotlinx.android.synthetic.main.activity_select_book.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectBookActivity : AppCompatActivity() {

    lateinit var adapter: BookSearchAdaptor
    var responseBookResult: BookResult? = null

    //------------------------ retrofit ------------------------
    // API 입력 변수들, INPUT_QUERY는 사용자 입력받아서 넣기
    val CLIENT_ID = "l4akKIkvGwlAUS_ZK4jm"
    val CLIENT_SECRET = "N3KjkHM1Yw"
    var INPUT_QUERY = ""

    val retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(BookSearchAPI::class.java)
    //------------------------ retrofit end --------------------


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_book)

        book_btn.setOnClickListener{

            // 레이아웃 매니저
            mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            mRecyclerView.setHasFixedSize(true)

            retrofitNaverCommunication() // retrofit - Naver API 통신: responseBookList에 response.body() 담음

            //키보드를 내린다.
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(book_input.windowToken, 0)


        } // 버튼 클릭 리스너 끝

    } // onCreate() 끝

    fun retrofitNaverCommunication() {
        // 사용자 입력어 넣기
        INPUT_QUERY = book_input.text.toString()

        api.getBookInfo(CLIENT_ID, CLIENT_SECRET, INPUT_QUERY)
            .enqueue(object: Callback<BookResult> {
                override fun onFailure(call: Call<BookResult>, t: Throwable) {

                    Log.e("책 정보 받기 실패", "통신 오류 발생"+t?.toString())
                }

                override fun onResponse(call: Call<BookResult>, response: Response<BookResult>) {

                    Log.d("책 정보 받기 성공", "통신 성공")
                    responseBookResult = response.body() // <BookResult>

                    runOnUiThread{
                        adapter = BookSearchAdaptor(responseBookResult!!)
                        mRecyclerView.adapter = adapter
                        book_input.setText("")
                    }

                    adapter.setItemClickListener(object: BookSearchAdaptor.ItemClickListener{
                        override fun onClick(view: View, position:Int){
                            Log.d("clicked", "${position}번")

                            sendBookInfoIntent(responseBookResult?.items?.get(position))


                        }
                    })



                }
            })


    }

    fun sendBookInfoIntent(book: Items?) {
        val Intent = Intent(this, WriteReviewActivity::class.java)
        Intent.putExtra("title", "${book?.title}")
        Intent.putExtra("author", "${book?.author}")
        Intent.putExtra("image", "${book?.image}")
        Intent.putExtra("publisher", "${book?.publisher}")
        Intent.putExtra("pubdate", "${book?.pubdate}")
        Intent.putExtra("isbn", "${book?.isbn}")
        startActivity(Intent)
    }



}


package com.alexna.booktu.dataAdpater

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 네이버 API 그대로 만듦: BookResult, Items
/*----------------------------------------------------*/
data class BookResult (
    var items: List<Items>
)

data class Items (
    var title:String = "",
    var image:String = "",
    var author:String = "",
    var publisher:String = "",
    var pubdate:String = "",
    var isbn:String = ""
)
/*----------------------------------------------------*/


interface BookSearchAPI {
    @GET("v1/search/book.json")
    fun getBookInfo(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String

    ): Call<BookResult>
}
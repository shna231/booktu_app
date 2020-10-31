package com.alexna.booktu.dataAdpater

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class BookReviews (
    @SerializedName("data")
    var items: List<BookReview>
)


data class BookReview (
    @SerializedName("writer_id")
    var writer_id: String = "",
    @SerializedName("title")
    var title: String ="",
    @SerializedName("content")
    var content: String ="",
    @SerializedName("book_title")
    var book_title: String ="",
    @SerializedName("book_image")
    var book_image: String ="",
    @SerializedName("book_publisher")
    var book_publisher: String="",
    @SerializedName("book_author")
    var book_author: String=""
)

// MainActivity에서 독후감 리스트 보기용으로 사용
interface BookReviewAPI {
    @FormUrlEncoded
    @POST("getReviewList.php")
    fun getReviews(
        @Field("writer_id") writer_id:String
    ) : Call<BookReviews>
}

// WriteReviewActivity에서 독후감 등록용으로 사용
interface BookReviewInsertAPI {
    @FormUrlEncoded
    @POST("reviewinsert.php")
    fun insertReview(
        @Field("writer_id") writer_id:String,
        @Field("title") title:String,
        @Field("content") content:String,
        @Field("book_title") book_title: String,
        @Field("book_image") book_image: String,
        @Field("book_publisher") book_publisher: String,
        @Field("book_author") book_author: String
    ) : Call<String>
}

// ShowReviewActivity에서 책 정보 표시용으로 사용
interface BookInfoAPI {
    @FormUrlEncoded
    @POST("findBookInfo.php")
    fun getBookInfo(
        @Field("book_image") book_image: String
    ) : Call<BookReviews>
}

interface ReviewDeleteAPI {
    @FormUrlEncoded
    @POST("deleteReview.php")
    fun deleteReview(
        @Field("title") title: String
    ) : Call<BookReviews>
}



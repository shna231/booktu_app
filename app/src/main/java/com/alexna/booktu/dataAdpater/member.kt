
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


// JSON 포맷대로 MemberItem 정의
// JSON 포맷: {"data": [{"column1" : "1"}, {"column2" : "2"}]}

data class MemberItem (
    @SerializedName("data")
    var MemberDatalist:List<Member>
)

data class Member (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("nickname")
    var nickname: String = "",
    @SerializedName("email")
    var email: String =""
)


// 레트로핏에서 사용할 API 정의
interface MemberLoginAPI {
    @FormUrlEncoded
    @POST("login.php")
    fun getUserInfo(
        @Field("id") id: String,
        @Field("password") password: String
    ) : Call<MemberItem>
}

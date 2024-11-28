import java.time.LocalDateTime
import java.time.Duration
import java.time.format.DateTimeFormatter

//data class Post(
//    val imageUrl: String?,  // 이미지 URL이 nullable
//    val memberName: String, //utf변환 준비
//    val createdTime: String, //날짜 함수 준비
//    val postContent: String, //utf변환 준비
//    val category: String, //utf변환 준비 or 카테고리 함수
//    val amount: Int, //금액 함수 확인
//    val likes: Int, //Int형 조심
//    val comments: Int, //Int형 조심
//    var isLiked: Boolean = false
//)
data class Post(
    val title: String, //없는 값
    val content: String, //postContent
    val imgUrl: String?, //imageUrl
    val category: String, //category
    val amount: Long, //amount
    val likeCount: Int, //likes
    val userId: String, //저장 해둬야함
    val userNickname: String, //memberName
    val createdAt: String //createdTime
)
// 상대 시간 계산 함수
fun getRelativeTime(createdAt: String): String {
    // String을 LocalDateTime으로 변환
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val createdAtDateTime = LocalDateTime.parse(createdAt, formatter)

    val now = LocalDateTime.now()
    val duration = Duration.between(createdAtDateTime, now)

    return when {
        duration.toDays() > 0 -> {
            val days = duration.toDays()
            "$days 일 전"
        }
        duration.toHours() > 0 -> {
            val hours = duration.toHours()
            "$hours 시간 전"
        }
        duration.toMinutes() > 0 -> {
            val minutes = duration.toMinutes()
            "$minutes 분 전"
        }
        else -> "방금 전"
    }
}

//// JSON을 Post 객체로 변환
//fun parsePost(json: String): Post {
//    val gson = Gson()
//    return gson.fromJson(json, Post::class.java)
//}

data class Post(
    val imageUrl: String?,  // 이미지 URL이 nullable
    val memberName: String, //utf변환 준비
    val createdTime: String, //날짜 함수 준비
    val postContent: String, //utf변환 준비
    val category: String, //utf변환 준비 or 카테고리 함수
    val amount: Int, //금액 함수 확인
    val likes: Int, //Int형 조심
    val comments: Int, //Int형 조심
    var isLiked: Boolean = false
)
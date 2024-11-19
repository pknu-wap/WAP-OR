data class Comment(
    val memberName: String,         // 작성자 이름
    val createdTime: String,        // 생성 시간 (날짜 함수와 연동 가능)
    val commentContent: String,    // 댓글 내용
    val comments: Int               // 댓글 수 (정수형)
)
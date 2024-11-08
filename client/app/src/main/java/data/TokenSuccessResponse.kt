package data
data class TokenSuccessResponse(
    val userId: Int,
    val userType: String,
    val identifier: String,
    val nickname: String,
    val createdAt: String,
    val lastLogin: String
)

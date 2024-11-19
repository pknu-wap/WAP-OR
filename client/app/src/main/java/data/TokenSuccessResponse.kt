package data
data class user(
    val identifier: String,
    val userType: String,
    val password: String?,
    val nickname: String,
    val refreshToken: String?,
    val createdAt: String,
    val lastLogin: String?
)
data class TokenSuccessResponse(
    val token: String,
    val user: user,
)

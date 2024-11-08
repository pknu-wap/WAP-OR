package data

data class TokenErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String
)

package com.example.wap_or.model
data class User(
    val id: Int,
    val identifier: String,
    val userType: String
)
data class LoginResponse(
    val message: String,
    val user: User
)

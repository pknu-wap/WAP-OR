package com.example.wap_or.model

data class LoginRequest(
    val identifier: String,
    val password: String,
    val userType: String = "EMAIL"
)
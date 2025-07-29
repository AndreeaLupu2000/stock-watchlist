package com.stock.watchlist.dto

//Register request DTO for the backend
data class RegisterRequest(
    val username: String,
    val password: String
)

//Login request DTO for the backend
data class LoginRequest(
    val username: String,
    val password: String
)
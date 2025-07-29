package com.stock.watchlist.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users", schema = "stock")
data class User(
    @Id
    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
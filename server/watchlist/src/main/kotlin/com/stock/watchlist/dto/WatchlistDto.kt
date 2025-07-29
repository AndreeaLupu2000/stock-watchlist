package com.stock.watchlist.dto

import java.time.LocalDateTime

//Watchlist DTO for the backend
data class WatchlistDto(
    val username: String
)

//Stock DTO for the backend
data class StockResponseDto(
    val symbol: String,
    val companyName: String,
    val price: Double
)

//Watchlist item DTO for the backend
data class WatchlistItemResponseDto(
    val id: Long,
    val createdAt: LocalDateTime,
    val stock: StockResponseDto,
    val username: String
)
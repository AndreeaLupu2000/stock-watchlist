package com.stock.watchlist.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "watchlist_items", schema = "stock")
data class WatchlistItem(
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "added_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_symbol", referencedColumnName = "symbol")
    val stock: Stock,

    @Column(name = "username")
    val username: String
)
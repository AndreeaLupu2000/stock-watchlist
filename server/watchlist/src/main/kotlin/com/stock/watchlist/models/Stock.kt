package com.stock.watchlist.models

import jakarta.persistence.*

@Entity
@Table(name = "stocks", schema = "stock")
data class Stock(
    @Id 
    @Column(name = "symbol")
    val symbol: String,
    
    @Column(name = "company_name")
    val companyName: String,
    
    @Column(name = "price")
    val price: Double
)

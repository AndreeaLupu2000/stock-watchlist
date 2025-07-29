package com.stock.watchlist.repositories

import com.stock.watchlist.models.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.Modifying

/**
 * Stock repository for the backend
 */
interface StockRepository : JpaRepository<Stock, String> {

    /**
     * Find stocks by symbol or company name
     * @param query - The query to search for
     * @param name - The name of the stock
     * @return List of stocks
     */
    @Query("SELECT s FROM Stock s WHERE s.symbol = :query OR LOWER(s.companyName) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun findBySymbolOrCompanyName(query: String, name: String): List<Stock>

    /**
     * Update a stock
     * @param symbol - The symbol of the stock
     * @param companyName - The company name of the stock
     * @param price - The price of the stock
     * @return Number of rows updated
     */
    @Modifying
    @Query("UPDATE Stock s SET s.price = :price, s.companyName = :companyName WHERE s.symbol = :symbol")
    fun updateStock(symbol: String, companyName: String, price: Double): Int
}
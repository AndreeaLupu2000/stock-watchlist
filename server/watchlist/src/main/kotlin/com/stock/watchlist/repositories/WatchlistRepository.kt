package com.stock.watchlist.repositories

import com.stock.watchlist.models.Stock
import com.stock.watchlist.models.WatchlistItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * Watchlist repository for the backend to interact with the watchlist table
 */
interface WatchlistRepository : JpaRepository<WatchlistItem, Long> {

    /**
     * Find a watchlist item by username
     * @param username - The username of the user
     * @return List of watchlist items
     */
    @Query("SELECT w FROM WatchlistItem w WHERE w.username = :username")
    fun findByUsername(username: String): List<WatchlistItem>

    /**
     * Find a watchlist item by stock symbol
     * @param symbol - The symbol of the stock
     * @return List of watchlist items
     */
    @Query("SELECT w FROM WatchlistItem w WHERE w.stock.symbol = :symbol")
    fun findByStockSymbol(symbol: String): List<WatchlistItem>

    /**
     * Find a watchlist item by stock symbol and username
     * @param symbol - The symbol of the stock
     * @param username - The username of the user
     * @return Watchlist item
     */
    @Query("SELECT w FROM WatchlistItem w WHERE w.stock.symbol = :symbol AND w.username = :username")
    fun findByStockSymbolAndUsername(symbol: String, username: String): WatchlistItem?

    /**
     * Check if a watchlist item exists by stock symbol
     * @param symbol - The symbol of the stock
     * @return Boolean
     */
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WatchlistItem w WHERE w.stock.symbol = :symbol")
    fun existsByStockSymbol(symbol: String): Boolean

    /**
     * Delete a watchlist item by stock symbol
     * @param symbol - The symbol of the stock
     * @return void
     */
    @Query("DELETE FROM WatchlistItem w WHERE w.stock.symbol = :symbol")
    fun deleteByStockSymbol(symbol: String)
}

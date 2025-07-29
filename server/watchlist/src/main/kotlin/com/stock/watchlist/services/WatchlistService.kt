package com.stock.watchlist.services

import com.stock.watchlist.models.WatchlistItem
import com.stock.watchlist.repositories.WatchlistRepository
import com.stock.watchlist.repositories.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.stock.watchlist.services.UserService
import com.stock.watchlist.dto.WatchlistItemResponseDto
import com.stock.watchlist.dto.StockResponseDto

/**
 * Watchlist service for the backend
 */
@Service
@Transactional
class WatchlistService(
    private val watchlistRepository: WatchlistRepository,
    private val stockRepository: StockRepository,
    private val userService: UserService
) {

    /**
     * Get watchlist
     * @param username - The username of the user
     * @return List of watchlist items
     */
    fun getWatchlist(username: String): List<WatchlistItemResponseDto> {
        return try {
            //Check if the user exists and throw an error if they do not
            if (!userService.existsByUsername(username)) {
                throw RuntimeException("User not found")
            }

            //Get the watchlist items
            val watchlistItems = watchlistRepository.findByUsername(username)

            //Map the watchlist items to the response DTO
            watchlistItems.map { item ->
                WatchlistItemResponseDto(
                    id = item.id,
                    createdAt = item.createdAt,
                    stock = StockResponseDto(
                        symbol = item.stock.symbol,
                        companyName = item.stock.companyName,
                        price = item.stock.price
                    ),
                    username = item.username
                )
            }
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to get watchlist: ${error.message}")
        }
    }

    /**
     * Add stock to watchlist
     * @param symbol - The symbol of the stock
     * @param username - The username of the user
     * @return Watchlist item
     */
    fun addStockToWatchlist(symbol: String, username: String): WatchlistItem {
        //Check if the stock exists and throw an error if it does not
        val stock = stockRepository.findById(symbol)
            .orElseThrow { RuntimeException("Stock with symbol $symbol not found") }
        
        //Check if the user exists and throw an error if they do not
        if (!userService.existsByUsername(username)) {
            throw RuntimeException("User not found")
        }

        //Check if the stock is already in the watchlist and throw an error if it is
        if (watchlistRepository.findByStockSymbolAndUsername(symbol, username) != null) {
            throw RuntimeException("Stock $symbol is already in watchlist for user $username")
        }
        
        return watchlistRepository.save(WatchlistItem(stock = stock, username = username))
    }

    /**
     * Remove stock from watchlist
     * @param symbol - The symbol of the stock
     * @param username - The username of the user
     * @return void
     */
    fun removeStockFromWatchlist(symbol: String, username: String) {
        //Check if the stock is in the watchlist and throw an error if it is not
        val watchlistItem = watchlistRepository.findByStockSymbolAndUsername(symbol, username)
            ?: throw RuntimeException("Stock $symbol not found in watchlist for user $username")

        watchlistRepository.delete(watchlistItem)
    }
}

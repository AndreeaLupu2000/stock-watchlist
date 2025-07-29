package com.stock.watchlist.controllers

import com.stock.watchlist.services.WatchlistService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import com.stock.watchlist.models.WatchlistItem
import com.stock.watchlist.services.UserService
import jakarta.validation.Valid

import com.stock.watchlist.dto.WatchlistDto
import com.stock.watchlist.dto.WatchlistItemResponseDto

/**
 * Watchlist controller for the backend
 */
@RestController
@RequestMapping("/api/watchlist")
@CrossOrigin(origins = ["*"])
class WatchlistController(
    private val watchlistService: WatchlistService,
    private val userService: UserService
) {

    /**
     * Get watchlist
     * @param username - The username of the user
     * @return ResponseEntity with the watchlist
     */
    @GetMapping("/{username}")
    fun getWatchlist(@PathVariable username: String) : ResponseEntity<List<WatchlistItemResponseDto>> {
        return try {
            //Get the watchlist
            val watchlist = watchlistService.getWatchlist(username)
            //Return the watchlist with the corresponding status code
            ResponseEntity.status(HttpStatus.OK).body(watchlist)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Add stock to watchlist
     * @param symbol - The symbol of the stock
     * @param watchlistDto - The watchlist DTO
     * @return ResponseEntity with the added stock
     */
    @PostMapping("/{symbol}")
    fun addStock(@PathVariable symbol: String, @Valid @RequestBody watchlistDto: WatchlistDto): ResponseEntity<Any> {
        return try {
            //Add the stock to the watchlist
            val stock = watchlistService.addStockToWatchlist(symbol, watchlistDto.username)
            //Return the added stock with the corresponding status code
            ResponseEntity.status(HttpStatus.CREATED).body(stock)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Remove stock from watchlist
     * @param symbol - The symbol of the stock
     * @param watchlistDto - The watchlist DTO
     * @return ResponseEntity with the removed stock
     */
    @DeleteMapping("/{symbol}")
    fun removeStock(@PathVariable symbol: String, @Valid @RequestBody watchlistDto: WatchlistDto): ResponseEntity<Any> {
        return try {
            //Remove the stock from the watchlist
            watchlistService.removeStockFromWatchlist(symbol, watchlistDto.username)
            //Return the removed stock with the corresponding status code
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }
}

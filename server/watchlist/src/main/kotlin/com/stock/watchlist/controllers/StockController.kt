package com.stock.watchlist.controllers

import com.stock.watchlist.services.StockService
import com.stock.watchlist.services.YahooFinanceService
import com.stock.watchlist.models.Stock
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

/**
 * Stock controller for the backend
 */
@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = ["*"])
class StockController(
    private val stockService: StockService,
    private val yahooFinanceService: YahooFinanceService
) {
    
    /**
     * Create a new stock
     * @param symbol - The symbol of the stock
     * @param companyName - The company name of the stock
     * @param price - The price of the stock
     * @return ResponseEntity with the created stock
     */
    @PostMapping("/")
    fun createStock(@Valid @RequestBody symbol: String, companyName: String, price: Double ): ResponseEntity<Stock>{
        return try {
            //Create the stock
            val stock = stockService.createStock(symbol, companyName, price)
            //Return the created stock with the corresponding status code
            ResponseEntity.status(HttpStatus.CREATED).body(stock)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Get all stocks
     * @return ResponseEntity with the list of stocks
     */
    @GetMapping("/")
    fun getAllStocks() : ResponseEntity<List<Stock>> {
        return try {
            //Get all stocks
            val stocks = stockService.getAllStocks()
            //Return the list of stocks with the corresponding status code
            ResponseEntity.status(HttpStatus.OK).body(stocks)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }
    
    /**
     * Search for stocks
     * @param query - The query to search for
     * @return ResponseEntity with the list of stocks
     */
    @GetMapping("/search")
    fun searchStocks(@RequestParam query: String): ResponseEntity<List<Stock>> {
        return try{
            //Search for Yahoo Finance stocks
            val apiResults = yahooFinanceService.searchStock(query)

            //Check if the API results are not null and the status code is OK
            if (apiResults.statusCode == HttpStatus.OK && apiResults.body != null) {
                //Get the stocks from the API results
                val stocks = apiResults.body!!

                //Create the stocks in the database if the price is not 0
                for (stock in stocks) {
                    if (stock.price != 0.0) {
                        stockService.createStock(stock.symbol, stock.companyName, stock.price)
                    }
                }
            }
            
            //Search for stocks in the database based on the symbol or company name
            //To display also old stocks and new stocks if the API returned new/updated stocks
            val databaseResults = stockService.searchStocks(query)
            //Return the list of stocks with the corresponding status code
            ResponseEntity.status(HttpStatus.OK).body(databaseResults)
        } catch(error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }
}

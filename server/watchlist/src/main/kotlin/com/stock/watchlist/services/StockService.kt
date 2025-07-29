package com.stock.watchlist.services

import com.stock.watchlist.models.Stock
import com.stock.watchlist.repositories.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Stock service for the backend
 */
@Service
class StockService(
    private val stockRepository: StockRepository
) {

    /**
     * Create a new stock
     * @param symbol - The symbol of the stock
     * @param companyName - The company name of the stock
     * @param price - The price of the stock
     * @return Stock
     */
    @Transactional
    fun createStock(symbol: String, companyName: String, price: Double): Stock {
        return try {
            //Check if the price is 0
            if (price == 0.0) {
                throw RuntimeException("Price cannot be 0")
            }
            
            //Check if the stock already exists
            val existingStock = stockRepository.findBySymbolOrCompanyName(symbol, symbol)
            if (existingStock.isNotEmpty()) {
                //Update existing stock price
                stockRepository.updateStock(symbol, companyName, price)
                //Return the updated stock
                existingStock[0]
            } else {
                //If the stock does not exist, create a new stock
                val newStock = Stock(
                    symbol = symbol,
                    companyName = companyName,
                    price = price,
                )

                //Save the new stock
                stockRepository.save(newStock)
                //Return the new stock
                newStock
            }
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to create stock: ${error.message}")
        }
    }

    /**
     * Get all stocks
     * @return List of stocks
     */
    fun getAllStocks(): List<Stock> {
        return try {
            //Get all stocks
            stockRepository.findAll()
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to get all stocks: ${error.message}")
        }
    }
    
    /**
     * Search for stocks
     * @param query - The query to search for
     * @return List of stocks
     */
    fun searchStocks(query: String): List<Stock> {
        return try {
            //Search for stocks
            stockRepository.findBySymbolOrCompanyName(query, query)
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to search stocks: ${error.message}")
        }
    }

}


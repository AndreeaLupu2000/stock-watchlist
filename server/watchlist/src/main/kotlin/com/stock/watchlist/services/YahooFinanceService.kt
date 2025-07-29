package com.stock.watchlist.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.stock.watchlist.models.Stock
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Yahoo Finance service for the backend
 */
@Service
class YahooFinanceService(
    private val objectMapper: ObjectMapper,
    @Value("\${rapidapi.key}") private val rapidApiKey: String,
    @Value("\${rapidapi.host}") private val rapidApiHost: String,
    @Value("\${rapidapi.base.url}") private val baseUrl: String
) {
    
    // OkHttpClient for the backend
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Search for stocks in a specific order
     * @param query - The query to search for
     * @return ResponseEntity with the list of stocks
     */
    fun searchStock(query: String): ResponseEntity<List<Stock>> {
        //Create  a list to store the results
        val results = mutableListOf<Stock>()
        
        // Step 1: Search for matches by the exact symbol using the  market/v2/get-quotes endpoint
        //If this returns a result, it means that the user entered the symbol
        val exactMatch = getStockQuote(query)
        //Check if the exact match is found and add it to the results
        if (exactMatch.statusCode == HttpStatus.OK && exactMatch.body != null) {
            results.add(exactMatch.body!!)
            return ResponseEntity.status(HttpStatus.OK).body(results)
        }

        // Step 2: If the user entered the company name, search for matches by the company name using the auto-complete endpoint
        val autoCompleteResults = searchStocksByName(query)
        //Check if the auto complete results are found and add them to the results
        if (autoCompleteResults.statusCode == HttpStatus.OK && autoCompleteResults.body != null) {
            results.addAll(autoCompleteResults.body!!)
            return ResponseEntity.status(HttpStatus.OK).body(results)
        }

        //If no results are found, return an empty list
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
    }

    /**
     * Get a stock quote
     * @param symbol - The symbol of the stock
     * @return ResponseEntity with the stock
     */
    fun getStockQuote(symbol: String): ResponseEntity<Stock> {
        return try {
            //Create the URL for the request
            val url = "$baseUrl/market/v2/get-quotes?region=US&symbols=$symbol"
            
            //Create the request
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", rapidApiKey)
                .addHeader("x-rapidapi-host", rapidApiHost)
                .build()

            //Execute the request
            val response = client.newCall(request).execute()

            //Get the response body
            val responseBody = response.body?.string() ?: ""

            //Check if the response is successful and the body is not blank
            if (response.isSuccessful && responseBody.isNotBlank()) {
                //Parse the quote from the market/v2/get-quotes endpoint
                val parseResult = parseQuoteFromMarketData(responseBody, symbol)
                //Check if the parse result is not null and return the stock with the corresponding status code
                return if (parseResult.body != null) {
                    ResponseEntity.status(HttpStatus.OK).body(parseResult.body!!)
                } 
                //If the parse result is null, return a not found status code
                else {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            }
        } catch (e: Exception) {
            println("Exception getting stock quote: ${e.message}")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    /**
     * Parse the quote from the market/v2/get-quotes endpoint
     * @param response - The response from the market/v2/get-quotes endpoint
     * @param symbol - The symbol of the stock
     * @return ResponseEntity with the stock
     */
    private fun parseQuoteFromMarketData(response: String, symbol: String): ResponseEntity<Stock> {
        return try {
            //Parse the response
            val jsonNode = objectMapper.readTree(response)

            //Get the quote response
            val quoteResponse = jsonNode.get("quoteResponse")

            //Get the result
            val result = quoteResponse?.get("result")

            //Check if the result is not null and is an array and has more than 0 elements
            if (result != null && result.isArray && result.size() > 0) {
                //Get the stock data
                val stockData = result.get(0)

                //Get the quote summary
                val quoteSummary = stockData.get("quoteSummary")

                //Get the summary detail
                val summaryDetail = quoteSummary?.get("summaryDetail")

                //Get the company name
                val companyName = stockData.get("longName")?.asText() 
                    ?: stockData.get("shortName")?.asText() 
                    ?: symbol 

                //Get the current price
                val currentPrice = summaryDetail?.get("regularMarketPrice")?.asDouble()
                    ?: summaryDetail?.get("previousClose")?.asDouble()
                    ?: stockData.get("regularMarketPrice")?.asDouble()
                    ?: 0.0
                
                //Return the stock with the corresponding status code
                return ResponseEntity.status(HttpStatus.OK).body(Stock(
                    symbol = symbol,
                    companyName = companyName,
                    price = currentPrice
                ))
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        } catch (e: Exception) {
            println("Error parsing quote from market data: ${e.message}")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    /**
     * Search for stocks by name
     * @param query - The query to search for
     * @return ResponseEntity with the list of stocks
     */
    private fun searchStocksByName(query: String): ResponseEntity<List<Stock>> {
        return try {
            //Create the URL for the request
            val url = "$baseUrl/auto-complete?q=$query&region=US"
            
            //Create the request
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", rapidApiKey)
                .addHeader("x-rapidapi-host", rapidApiHost)
                .build()

            //Execute the request
            val response = client.newCall(request).execute()

            //Get the response body
            val responseBody = response.body?.string() ?: ""

            //Check if the response is successful and the body is not blank
            if (response.isSuccessful && responseBody.isNotBlank()) {
                //Parse the auto complete results
                val results = parseAutoCompleteResults(responseBody)    
                //Check if the results are not null and the status code is OK and return the results with the corresponding status code
                return if (results.statusCode == HttpStatus.OK && results.body != null) {
                    ResponseEntity.status(HttpStatus.OK).body(results.body!!)
                } 
                //If the results are null, return a not found status code
                else {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
            }
        } catch (e: Exception) {
            println("Exception in autocomplete search: ${e.message}")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }

    /**
     * Parse the auto complete results
     * @param response - The response from the auto-complete endpoint
     * @return ResponseEntity with the list of stocks
     */
    private fun parseAutoCompleteResults(response: String): ResponseEntity<List<Stock>> {
        return try {
            //Parse the response
            val jsonNode = objectMapper.readTree(response)

            //Get the quotes
            val quotes = jsonNode.get("quotes")

            //Create a list to store the results
            val results = mutableListOf<Stock>()
            
            //Check if the quotes are not null and is an array
            if (quotes != null && quotes.isArray) {
                //Loop through the quotes
                for (quote in quotes) {
                    //Get the symbol
                    val symbol = quote.get("symbol")?.asText()
                    //Check if the symbol is not null
                    if (symbol != null ) {
                        //Get the information that we need for the stock, aka price and symbol
                        val stockResponse = getStockQuote(symbol)
                        //Check if the stock response is not null and the status code is OK
                        if (stockResponse.statusCode == HttpStatus.OK && stockResponse.body != null) {
                            //Get the stock
                            val stock = stockResponse.body!!
                            //Add the stock to the results
                            results.add(Stock(symbol, stock.companyName, stock.price))
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(results)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }
}
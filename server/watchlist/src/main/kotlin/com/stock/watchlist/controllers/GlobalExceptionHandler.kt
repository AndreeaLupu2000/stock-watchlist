package com.stock.watchlist.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Global exception handler for the backend
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handle runtime exceptions
     * @param ex - The runtime exception
     * @return ResponseEntity with the error message and the status code
     */
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "Unknown error")))
    }
} 
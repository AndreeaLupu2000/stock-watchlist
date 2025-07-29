package com.stock.watchlist.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Cors configuration for the backend
 */
@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        //Cors configuration
        val configuration = CorsConfiguration()
        //Allowed origin patterns for the frontend
        configuration.allowedOriginPatterns = listOf("http://localhost:5173")
        //Allowed methods
        configuration.allowedMethods = listOf("GET", "POST", "DELETE", "PUT", "OPTIONS")
        //Allowed headers
        configuration.allowedHeaders = listOf("*")  
        //Allow credentials
        configuration.allowCredentials = true
        
        //Source configuration for the cors configuration
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
} 
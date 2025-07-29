package com.stock.watchlist.repositories

import com.stock.watchlist.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * User repository for the backend
 */
interface UserRepository : JpaRepository<User, Long> {

    /**
     * Find a user by username
     * @param username - The username of the user
     * @return Optional of the user
     */
    fun findByUsername(username: String): Optional<User>

    /**
     * Check if a user exists by username
     * @param username - The username of the user
     * @return Boolean
     */
    fun existsByUsername(username: String): Boolean

    /**
     * Delete a user by username
     * @param username - The username of the user
     * @return void
     */
    fun deleteByUsername(username: String)
}
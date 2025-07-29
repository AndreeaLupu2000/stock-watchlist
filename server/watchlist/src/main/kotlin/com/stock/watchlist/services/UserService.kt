package com.stock.watchlist.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.stock.watchlist.repositories.UserRepository
import com.stock.watchlist.models.User
import org.mindrot.jbcrypt.BCrypt

/**
 * User service for the backend
 */
@Service
@Transactional
class UserService(private val userRepository: UserRepository) {

    /**
     * Create a new user
     * @param username - The username of the user
     * @param password - The password of the user
     * @return User
     */
    fun createUser(username: String, password: String): User {
        return try {
            //Check if the username and password are blank and throw an error if they are
            if (username.isBlank() || password.isBlank()) {
                throw RuntimeException("Username and password are required")
            }

            //Check if the username already exists and throw an error if it does
            if (userRepository.existsByUsername(username)) {
                throw RuntimeException("Username already exists")
            }

            //Hash the password
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            //Create a new user
            val user = User(username = username, passwordHash = hashedPassword)
            //Save the user
            userRepository.save(user)
            //Return the user
            user
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to create user: ${error.message}")
        }
    }

    /**
     * Login a user
     * @param username - The username of the user
     * @param password - The password of the user
     * @return User
     */
    fun login(username: String, password: String): User {
        return try {
            //Find the user by username
            val userOptional = userRepository.findByUsername(username)

            //Check if the user exists and throw an error if they do not
            if (userOptional.isEmpty) {
                throw RuntimeException("User not found")
            }
            
            //Get the user
            val user = userOptional.get()

            //Check if the password is correct and throw an error if it is not
            if (!BCrypt.checkpw(password, user.passwordHash)) {
                throw RuntimeException("Invalid password")
            }

            //Return the user
            user
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to login: ${error.message}")
        }
    }

    /**
     * Delete a user
     * @param username - The username of the user
     * @return void
     */
    fun deleteUser(username: String) {
        return try {
            //Check if the user exists and throw an error if they do not
            if (!userRepository.existsByUsername(username)) {
                throw RuntimeException("User not found")
            }

            userRepository.deleteByUsername(username)
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to delete user: ${error.message}")
        }
    }

    /**
     * Get a user
     * @param username - The username of the user
     * @return User
     */
    fun getUser(username: String): User {
        return try {
            //Find the user by username
            val userOptional = userRepository.findByUsername(username)
            
            //Check if the user exists and throw an error if they do not
            if (userOptional.isEmpty) {
                throw RuntimeException("User not found")
            }
            
            //Return the user
            userOptional.get()
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to get user: ${error.message}")
        }
    }
    
    /**
     * Check if a user exists by username
     * @param username - The username of the user
     * @return Boolean
     */
    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    /**
     * Get all users
     * @return List of users
     */
    fun getAllUsers(): List<User> {
        return try {
            //Get all users
            userRepository.findAll()?: emptyList()
        } catch (error: RuntimeException) {
            throw RuntimeException("Failed to get all users: ${error.message}")
        }
    }
}
package com.stock.watchlist.controllers

import com.stock.watchlist.services.UserService
import com.stock.watchlist.models.User
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import com.stock.watchlist.dto.RegisterRequest
import com.stock.watchlist.dto.LoginRequest



/**
 * User controller for the backend
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService
){

    /**
     * Create a new user
     * @param registerRequest - The register request
     * @return ResponseEntity with the created user
     */
    @PostMapping("/register")
    fun createUser(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<User> {
        return try {
            //Create the user
            val user = userService.createUser(registerRequest.username, registerRequest.password)
            //Return the created user with the corresponding status code
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Login a user
     * @param loginRequest - The login request
     * @return ResponseEntity with the logged in user
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<User> {
        return try {
            //Login the user
            val user = userService.login(loginRequest.username, loginRequest.password)
            //Return the logged in user with the corresponding status code
            ResponseEntity.status(HttpStatus.OK).body(user)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Delete a user
     * @param username - The username of the user
     * @return ResponseEntity with the deleted user
     */
    @DeleteMapping("/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Void> {
        return try {
            //Delete the user
            userService.deleteUser(username)
            //Return the deleted user with the corresponding status code
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Get a user
     * @param username - The username of the user
     * @return ResponseEntity with the user
     */
    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): ResponseEntity<User> {
        return try {
            //Get the user
            val user = userService.getUser(username)
            //Return the user with the corresponding status code
            ResponseEntity.status(HttpStatus.OK).body(user)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }

    /**
     * Get all users
     * @return ResponseEntity with the list of users
     */
    @GetMapping("/")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return try {
            //Get all users
            val users = userService.getAllUsers()
            //Return the list of users with the corresponding status code
            ResponseEntity.status(HttpStatus.OK).body(users)
        } catch (error: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, error.message)
        }
    }


}

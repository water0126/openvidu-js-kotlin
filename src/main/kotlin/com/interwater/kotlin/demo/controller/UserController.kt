package com.interwater.kotlin.demo.controller

import com.interwater.kotlin.demo.model.User
import com.interwater.kotlin.demo.repository.UserRepository
import com.interwater.kotlin.demo.util.Log
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
class UserController(private val userRepository: UserRepository) {
    @Log
    private lateinit var log: Logger

    @GetMapping("/user/")
    fun test(principal: Principal?) = principal?.name ?: "You are not logged in"

    @PostMapping("/user/register")
    @ResponseBody fun register(@RequestBody user: User): ResponseEntity<Any> { // You should hash users' passwords
        if (userRepository.findByUsername(user.username) != null) {
            try {
                return ResponseEntity("user existed", HttpStatus.CONFLICT)
            } catch (e: Exception){
                e.stackTrace
            }
        }
        val encoder = BCryptPasswordEncoder(16)
        val result = encoder.encode(user.password)
        userRepository.save(User(username = user.username, password = result))
        return ResponseEntity.ok("created")
    }
}
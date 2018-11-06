package com.interwater.kotlin.demo.repository

import com.interwater.kotlin.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
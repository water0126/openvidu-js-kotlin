package com.interwater.kotlin.demo.service

import com.interwater.kotlin.demo.model.User
import com.interwater.kotlin.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by quangio.
 */

@Service
class SecurityUserDetailsService @Autowired
constructor(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val u: User = userRepository.findByUsername(s) ?: throw(UsernameNotFoundException("Username not found"))
        return SecurityUserDetails(u)
    }
}
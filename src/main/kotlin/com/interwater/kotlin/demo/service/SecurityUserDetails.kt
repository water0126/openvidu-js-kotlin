package com.interwater.kotlin.demo.service

import com.interwater.kotlin.demo.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

/**
 * Created by quangio.
 */
class SecurityUserDetails(val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.roles.mapTo(LinkedList<GrantedAuthority>()) { SimpleGrantedAuthority(it.role) }

    override fun getPassword(): String = user.password!!

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

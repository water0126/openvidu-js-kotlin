package com.interwater.kotlin.demo.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.interwater.kotlin.demo.model.ResponseResult
import com.interwater.kotlin.demo.model.User
import com.interwater.kotlin.demo.service.SecurityUserDetails
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by quangio.
 */

class JWTAuthenticationFilter : UsernamePasswordAuthenticationFilter {
    constructor(authenticationManager: AuthenticationManager) : super() {
        this.authenticationManager = authenticationManager
        super.setFilterProcessesUrl("/auth/login")
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse?): Authentication? {

        try {
            println(request.method)
            val loginUser = ObjectMapper().readValue(request.inputStream, User::class.java)
            println(loginUser.username);
            println(loginUser.password);
            println(authenticationManager);
            return authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(loginUser.username, loginUser.password)
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          chain: FilterChain?,
                                          authResult: Authentication) {
        val securityUserDetails = (authResult.principal as SecurityUserDetails)
        var role = ""
        var loginName = ""
        val authorities = securityUserDetails.getAuthorities()
        for (authority in authorities) {
            role = authority.getAuthority()
        }

        val token = JwtTokenUtils.createToken(securityUserDetails.getUsername(), role)
        Gson().toJson(ResponseResult(hashMapOf("token" to JwtTokenUtils.TOKEN_PREFIX + token, "loginName" to securityUserDetails.user.username)), response.writer)
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token)
    }

    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        Gson().toJson(ResponseResult(403, "authentication failed, reason: " + failed.message), response.writer)
    }

}
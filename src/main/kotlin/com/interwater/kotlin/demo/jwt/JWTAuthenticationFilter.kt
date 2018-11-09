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
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by quangio.
 */

class JWTAuthenticationFilter : UsernamePasswordAuthenticationFilter {
    private val rememberMe: ThreadLocal<Boolean>

    constructor(authenticationManager: AuthenticationManager) : super() {
        this.authenticationManager = authenticationManager
        this.rememberMe = ThreadLocal<Boolean>()
        super.setFilterProcessesUrl("/auth/login")
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse?): Authentication? {

        // 从输入流中获取到登录的信息
        try {
            val loginUser = ObjectMapper().readValue(request.inputStream, User::class.java)
            println("-------");
            println(loginUser.username);
            println(loginUser.password);
            println(loginUser.rememberMe);
            println(authenticationManager);
            println("-------");
            rememberMe.set(loginUser.rememberMe)
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
        System.out.println("jwtUser:" + securityUserDetails.toString())
        val isRemember = rememberMe.get() == true

        var role = ""
        val authorities = securityUserDetails.getAuthorities()
        for (authority in authorities) {
            role = authority.getAuthority()
        }

        val token = JwtTokenUtils.createToken(securityUserDetails.getUsername(), role, isRemember)
        Gson().toJson(ResponseResult(hashMapOf("token" to JwtTokenUtils.TOKEN_PREFIX + token)), response.writer)
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token)
    }

    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        Gson().toJson(ResponseResult(403, "authentication failed, reason: " + failed.message), response.writer)
    }

}
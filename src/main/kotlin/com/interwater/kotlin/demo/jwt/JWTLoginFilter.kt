package com.interwater.kotlin.demo.jwt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.interwater.kotlin.demo.config.database.ReadOnlyConnection
import com.interwater.kotlin.demo.jwt.JWTUtils.createJwt
import com.interwater.kotlin.demo.service.SecurityUserDetails
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by quangio.
 */
class JWTLoginFilter internal constructor(s: String, authenticationManager: AuthenticationManager) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(s)) {
    init {
        setAuthenticationManager(authenticationManager)
    }

    @ReadOnlyConnection
    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(
            req: HttpServletRequest, res: HttpServletResponse): Authentication {
        val cred: AccountCredentials? = jacksonObjectMapper().readValue(req.inputStream)

        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        cred?.username,
                        cred?.password,
                        emptyList<GrantedAuthority>()
                )
        )
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse, chain: FilterChain?, auth: Authentication) {


        JWTUtils.addAuthentication(res, (auth.principal as SecurityUserDetails).user)
    }
}

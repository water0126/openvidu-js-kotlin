package com.interwater.kotlin.demo.jwt

import com.google.gson.Gson
import com.interwater.kotlin.demo.model.ResponseResult
import com.interwater.kotlin.demo.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

/**
 * Created by quangio.
 */

internal object JWTUtils {
    private val log = LoggerFactory.getLogger(JWTUtils::class.java)

    private val expiration: Long = 100L
    private val secret = "interwater"
    private val header = "Authorization"

    fun User.createJwt(): String {
        val claims = HashMap<String, Any>()
        claims.put("roles", this.roles.map{it.role})
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(this.username)
                .setExpiration(Date(Date().time + TimeUnit.HOURS.toMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret).compact()
    }

    fun addAuthentication(response: HttpServletResponse, user: User) {
        val jwt = user.createJwt()
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8083")
        val responseResult = ResponseResult(200, null, mapOf<String, String>("token" to jwt))
        Gson().toJson(responseResult, response.writer);
    }

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(header) ?: return null
        println(token)
        val tokenBefore = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
        val tokenBody = tokenBefore.body
        val username: String = tokenBody.subject
        @Suppress("UNCHECKED_CAST")
        val roles = tokenBody["roles"] as List<String>
        val res = roles.mapTo(LinkedList<GrantedAuthority>()) { SimpleGrantedAuthority(it) }
        log.info(username + " logged in with authorities " + res)
        return UsernamePasswordAuthenticationToken(username, null, res)
    }
}


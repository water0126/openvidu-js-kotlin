package com.interwater.kotlin.demo.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

internal object JwtTokenUtils {
    val TOKEN_HEADER = "Authorization"
    val TOKEN_PREFIX = "Bearer "

    private val SECRET = "mysecret"
    private val ISS = "interwater"

    private val ROLE_CLAIMS = "rol"

    private val EXPIRATION = 7200L

    fun createToken(username: String, role: String): String {
        val expiration = EXPIRATION
        val map = HashMap<String, Any>()
        map[ROLE_CLAIMS] = role
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + expiration * 1000))
                .compact()
    }

    fun getUsername(token: String): String {
        return getTokenBody(token).subject
    }

    fun getUserRole(token: String): String {
        return getTokenBody(token)[ROLE_CLAIMS] as String
    }

    fun isExpiration(token: String): Boolean {
        return getTokenBody(token).expiration.before(Date())
    }

    private fun getTokenBody(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .body
    }
}
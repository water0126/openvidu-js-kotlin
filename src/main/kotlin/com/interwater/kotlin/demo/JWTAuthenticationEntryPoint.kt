package com.interwater.kotlin.demo

import com.google.gson.Gson
import com.interwater.kotlin.demo.model.ResponseResult
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by echisan on 2018/6/24
 */
class JWTAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest,
                          response: HttpServletResponse,
                          authException: AuthenticationException) {

        response.characterEncoding = "UTF-8"
        response.contentType = "application/json; charset=utf-8"
        response.status = HttpServletResponse.SC_FORBIDDEN
        Gson().toJson(ResponseResult(401, authException.message!!), response.writer)
    }
}

package com.interwater.kotlin.demo.controller

import com.interwater.kotlin.demo.model.ResponseResult
import io.openvidu.java.client.OpenVidu
import io.openvidu.java.client.OpenViduRole
import io.openvidu.java.client.Session
import io.openvidu.java.client.TokenOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap


@RestController
@RequestMapping(value = ["/session"])
class SessionController {
    private var openVidu: OpenVidu

    // Collection to pair session names and OpenVidu Session objects
    private val mapSessions = ConcurrentHashMap<String, Session>()
    // Collection to pair session names and tokens (the inner Map pairs tokens and role associated)
    private val mapSessionNamesTokens = ConcurrentHashMap<String, Map<String, OpenViduRole>>()

    // URL where our OpenVidu server is listening
    private var OPENVIDU_URL: String
    // Secret shared with our OpenVidu server
    private var SECRET: String


    constructor(@Value("\${openvidu.secret}") secret: String, @Value("\${openvidu.url}") openviduUrl: String) {
        this.SECRET = secret
        this.OPENVIDU_URL = openviduUrl
        this.openVidu = OpenVidu(OPENVIDU_URL, SECRET)
    }

    @GetMapping
    @ResponseBody
    fun joinSession(authentication: Authentication, @RequestParam(name = "data") clientData: String,
                    @RequestParam(name = "session-name") sessionName: String): ResponseResult {
        val role = OpenViduRole.PUBLISHER
        val serverData = "{\"serverData\": \"" + authentication.principal + "\"}"
        val tokenOptions = TokenOptions.Builder().data(serverData).role(role).build()

        if (this.mapSessions[sessionName] != null) {
            System.out.println("Existing session $sessionName")
            try {
                val token = (this.mapSessions[sessionName] as Session).generateToken(tokenOptions)
                this.mapSessionNamesTokens[sessionName]!!.plus(mutableMapOf(token to role))
                return ResponseResult(mutableMapOf<String, String>("sessionName" to sessionName, "token" to token, "nickName" to clientData, "userName" to authentication.principal.toString()))

            } catch (e: Exception) {
                return ResponseResult(500, "error")
            }

        } else {
            println("New session $sessionName")
            try {
                val session = this.openVidu.createSession()
                val token = session.generateToken(tokenOptions)
                this.mapSessions[sessionName] = session
                this.mapSessionNamesTokens[sessionName] = mutableMapOf(token to role)
                return ResponseResult(mutableMapOf<String, String>("sessionName" to sessionName, "token" to token, "nickName" to clientData, "userName" to authentication.principal.toString()))
            } catch (e: Exception) {
                return ResponseResult(500, "error")
            }

        }
        return ResponseResult(200)
    }
}
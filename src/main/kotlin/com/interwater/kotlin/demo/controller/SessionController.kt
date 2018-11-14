package com.interwater.kotlin.demo.controller

import com.interwater.kotlin.demo.model.ResponseResult
import io.openvidu.java.client.OpenVidu
import io.openvidu.java.client.OpenViduRole
import io.openvidu.java.client.Session
import io.openvidu.java.client.TokenOptions
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
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
    private val mapSessionNamesTokens = ConcurrentHashMap<String, MutableMap<String, OpenViduRole>>()

    // URL where our OpenVidu server is listening
    private var OPENVIDU_URL: String
    // Secret shared with our OpenVidu server
    private var SECRET: String


    constructor(@Value("\${openvidu.secret}") secret: String, @Value("\${openvidu.url}") openviduUrl: String) {
        this.SECRET = secret
        this.OPENVIDU_URL = openviduUrl
        this.openVidu = OpenVidu(OPENVIDU_URL, SECRET)
    }

    /**
     * get token
     */
    @GetMapping
    @ResponseBody
    fun joinSession(authentication: Authentication, @RequestParam(name = "nickName") clientData: String,
                    @RequestParam(name = "sessionName") sessionName: String): ResponseResult {
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
                e.printStackTrace();
                return ResponseResult(500, "error")
            }

        }
        return ResponseResult(200)
    }

    @PostMapping("/remove-user")
    @ResponseBody
    fun removeUser(authentication: Authentication, @RequestBody sessionNameToken: String): ResponseResult {
        val sessionNameTokenJSON = JSONParser().parse(sessionNameToken) as JSONObject
        val sessionName = sessionNameTokenJSON["sessionName"] as String
        val token = sessionNameTokenJSON["token"] as String
        if (this.mapSessions[sessionName] != null && this.mapSessionNamesTokens[sessionName] != null) {
            if (this.mapSessionNamesTokens[sessionName]?.remove(token) != null) {
                // User left the session
                if (this.mapSessionNamesTokens[sessionName].isNullOrEmpty()) {
                    // Last user left: session must be removed
                    this.mapSessions.remove(sessionName)
                }
                return ResponseResult()
            } else {
                // The TOKEN wasn't valid
                println("Problems in the app server: the TOKEN wasn't valid")
                return ResponseResult(500, "Problems in the app server: the TOKEN wasn't valid")
            }

        } else {
            // The SESSION does not exist
            println("Problems in the app server: the SESSION does not exist")
            return ResponseResult(500, "Problems in the app server: the SESSION does not exist")
        }
    }

}
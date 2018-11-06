package com.interwater.kotlin.demo.controller

import com.interwater.kotlin.demo.jwt.JWTUtils
import com.interwater.kotlin.demo.model.ResponseResult
import com.interwater.kotlin.demo.model.User
import com.interwater.kotlin.demo.service.HelloService
import com.interwater.kotlin.demo.service.SecurityUserDetailsService
import com.interwater.kotlin.demo.util.Log
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @Autowired
    lateinit var helloService: HelloService
    @Autowired
    lateinit var securityUserDetailsService: SecurityUserDetailsService
    @Log
    private lateinit var log: Logger


    @GetMapping("/hello")
    fun hello(@RequestParam(value = "id") id: Int): ResponseResult {
        //~log.info(User)
        helloService.call()
        return ResponseResult(id)
    };


}
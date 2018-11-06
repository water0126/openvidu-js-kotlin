package com.interwater.kotlin.demo.service

import com.google.gson.Gson
import com.interwater.kotlin.demo.entity.TestEntity
import com.interwater.kotlin.demo.repository.TestRepository
import com.interwater.kotlin.demo.util.Log
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HelloService {
    @Log
    private lateinit var log: Logger

    @Autowired
    lateinit var testEntityRepository: TestRepository

    fun call() {
        var testEntity = testEntityRepository.findById(3608);
        log.info(Gson().toJson(testEntity))
    }
}
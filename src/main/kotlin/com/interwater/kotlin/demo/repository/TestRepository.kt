package com.interwater.kotlin.demo.repository

import com.interwater.kotlin.demo.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface TestRepository : JpaRepository<TestEntity, Long> {
    fun findByCreatedTimeGreaterThanAndBaseAndCounterAndStrategyNameOrderByIdAsc(createdTime: Date,
                                                                                 base: String, counter: String, strategyName: String): List<TestEntity>

    fun findByIsFinishedAndBaseAndCounterOrderByIdAsc(isFinished: Long?, base: String, counter: String): List<TestEntity>
    fun findByIsFinished(isFinished: Long?): List<TestEntity>
    fun findByIsFinishedGreaterThanOrderById(isFinished: Long?): List<TestEntity>
    fun findByIsFinishedIsNull(): List<TestEntity>

}
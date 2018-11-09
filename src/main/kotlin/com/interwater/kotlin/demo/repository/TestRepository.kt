package com.interwater.kotlin.demo.repository

import com.interwater.kotlin.demo.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface TestRepository : JpaRepository<TestEntity, Long> {
}
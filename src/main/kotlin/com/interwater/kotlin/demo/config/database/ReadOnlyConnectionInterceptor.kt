package com.interwater.kotlin.demo.config.database

import com.interwater.kotlin.demo.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.springframework.core.Ordered
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Aspect

class ReadOnlyConnectionInterceptor : Ordered {
    @Log
    private lateinit var log: Logger

    private var order: Int = 0
    private var entityManager: EntityManager? = null

    fun setOrder(order: Int) {
        this.order = order
    }

    override fun getOrder(): Int {
        return order
    }

    @PersistenceContext
    fun setEntityManager(entityManager: EntityManager) {
        this.entityManager = entityManager
    }

    @Around("@annotation(readOnlyConnection)")
    @Throws(Throwable::class)
    fun proceed(pjp: ProceedingJoinPoint, readOnlyConnection: ReadOnlyConnection): Any {

        val connection = entityManager!!.unwrap(java.sql.Connection::class.java)

        val autoCommit = connection.autoCommit
        val readOnly = connection.isReadOnly

        try {
            connection.autoCommit = false
            connection.isReadOnly = true
            return pjp.proceed()

        } finally {
            // restore state
            connection.isReadOnly = readOnly
            connection.autoCommit = autoCommit
        }
    }
}
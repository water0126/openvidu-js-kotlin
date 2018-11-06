package com.interwater.kotlin.demo.util

import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field


@Component
class LogInjector : BeanPostProcessor {

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        return bean
    }

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, name: String): Any {
        val throws = @kotlin.jvm.Throws(IllegalArgumentException::class, java.lang.IllegalAccessException::class) { field: Field ->
            // SAM conversion for Java interface
            ReflectionUtils.makeAccessible(field)
            if (field.getAnnotation(Log::class.java) != null) {
                val log = LoggerFactory.getLogger(bean.javaClass)
                field.set(bean, log)
            }
        }

        ReflectionUtils.doWithFields(bean.javaClass, throws)
        return bean
    }
}
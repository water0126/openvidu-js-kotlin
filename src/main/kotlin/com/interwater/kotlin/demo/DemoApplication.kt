package com.interwater.kotlin.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Bean
fun registerOpenSessionInViewFilterBean(): FilterRegistrationBean<*> {
    val filter = OpenSessionInViewFilter()
    val registrationBean = FilterRegistrationBean(filter)
    registrationBean.order = 5
    return registrationBean
}
package com.interwater.kotlin.demo.config

import com.interwater.kotlin.demo.jwt.JWTAuthenticationFilter
import com.interwater.kotlin.demo.jwt.JWTLoginFilter
import com.interwater.kotlin.demo.model.UserRole
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.SessionManagementFilter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.sessionManagement();
        http.csrf()
                .disable()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/user/**", "/browser/**", "/public/**").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/admin").hasAuthority(UserRole.ADMIN)
                .and()
                .addFilterBefore(CorsFilter(), SessionManagementFilter::class.java)
                .addFilterBefore(JWTLoginFilter("/user/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


}
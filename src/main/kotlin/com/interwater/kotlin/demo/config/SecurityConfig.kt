package com.interwater.kotlin.demo.config

import com.interwater.kotlin.demo.JWTAuthenticationEntryPoint
import com.interwater.kotlin.demo.jwt.JWTAuthenticationFilter
import com.interwater.kotlin.demo.jwt.JWTAuthorizationFilter
import com.interwater.kotlin.demo.model.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    @Qualifier("securityUserDetailsService")
    lateinit var userDetailsService: UserDetailsService

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }


    override fun configure(http: HttpSecurity) {
        http.headers().cacheControl()
        http.cors().and().csrf().disable()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/auth/**", "/browser/**", "/public/**").permitAll()
                .antMatchers("/admin/**").hasRole(UserRole.ADMIN)
                .antMatchers("/hello").hasRole(UserRole.GUEST)
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(JWTAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterAfter(JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter::class.java)
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(JWTAuthenticationEntryPoint())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = mutableListOf("*")
        configuration.allowedMethods = mutableListOf("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH")
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.allowCredentials = true
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.allowedHeaders = mutableListOf("Authorization", "Cache-Control", "Content-Type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


}
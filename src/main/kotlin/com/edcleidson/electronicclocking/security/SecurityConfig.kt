package com.edcleidson.electronicclocking.security

import com.edcleidson.electronicclocking.utils.helpers.BasicOperationsHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(val employeeDetailsService: EmployeeDetailsService) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider())
        super.configure(auth)
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.
//      .antMatchers(HttpMethod.POST, "/employees").permitAll()
        anyRequest()?.authenticated()?.and()?.httpBasic()?.and()?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()?.csrf()?.disable()?.headers()
            ?.frameOptions()?.sameOrigin()

    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider? {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(employeeDetailsService)
        authProvider.setPasswordEncoder(BasicOperationsHelper.bCryptPasswordEncoder)
        return authProvider
    }
}
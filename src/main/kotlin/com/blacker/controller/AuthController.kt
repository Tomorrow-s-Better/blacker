package com.blacker.controller

import com.blacker.security.JwtUtil
import com.blacker.service.MyUserDetailsService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: MyUserDetailsService
) {

    @PostMapping("/authenticate")
    fun createAuthenticationToken(@RequestBody authRequest: AuthRequest): String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        )

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(authRequest.username)
        return jwtUtil.generateToken(userDetails.username)
    }
}

data class AuthRequest(val username: String, val password: String)

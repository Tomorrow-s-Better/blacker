package com.blacker.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        // 실제로는 DB에서 유저 정보를 불러와야 함. 예시로 하드코딩된 유저 정보 사용.
        if (username == "testUser") {
            return User("testUser", "testPassword", ArrayList())
        }
        throw UsernameNotFoundException("User not found")
    }
}

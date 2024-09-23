package com.blacker.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    // TODO: lateinit을 사용하여 주입하는 방식으로 수정 (서비스 시작시마다 secret key 변경은 서비스에 이슈 발생요인)
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun extractUsername(token: String): String? {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
    }

    fun generateToken(username: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractClaim(token, Claims::getExpiration)
        return expiration.before(Date())
    }
}

package com.lguplus.assignment.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "abcdhew2140y823rh8932hfhg82hge132234g32g2g233g25"; // 편의상 이곳에 노출시켰음
    private static final Long EXPIRATION_TIME = 3600000L; // 1시간

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(Long memberId) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId)) // memberId를 문자열로 저장
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long validateToken(String token) {
        try {
            String jwt = extractToken(token); // Bearer 제거
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return Long.parseLong(claims.getSubject()); // memberId 반환
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("JWT 검증 실패");
        }
    }

    private String extractToken(String bearerToken) {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        } else {
            throw new RuntimeException("JWT 형식이 잘못되었습니다. Bearer 형식을 사용하세요.");
        }
    }
}

package com.zyt.xxt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private long expiration = 86400000; // 24小时

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();


        redisTemplate.opsForValue().set("token:" + username, token, expiration, TimeUnit.MILLISECONDS);
        return token;
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("无效的 Token", e);
        }
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token, String username) {
        String cachedToken = (String) redisTemplate.opsForValue().get("token:" + username);
        return token.equals(cachedToken) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}

package com.zyt.xxt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // 密钥建议使用更安全的 Key 类型，并可从配置文件加载
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private long expiration = 86400000; // 24小时

    // 生成token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // 解析token并返回完整 Claims
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            // 可记录日志或处理异常
            throw new IllegalArgumentException("无效的 Token", e);
        }
    }

    // 若需要获取用户名，单独提供方法
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }
}

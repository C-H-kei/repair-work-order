package com.dormitory.repairbackend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // 密钥，实际项目要写在配置文件里
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 过期时间：24小时
    private static final long EXPIRATION_TIME = 86400000;

    // 生成 Token
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    // 解析 Token (如果解析失败会抛异常)
    public static String parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
package com.huibai.eomp.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author zhouyihao
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 生成令牌
    public String createToken(Integer userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 提取所有的 Claims（核心方法）
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 获取用户名 (Subject)
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // 获取用户ID (Custom Claim)
    public Integer getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Integer.class);
    }

    // 校验令牌
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Token 的剩余存活时间（毫秒）
     * 解决：无法解析 'getExpireTime'
     */
    public long getExpireTime(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expirationDate = claims.getExpiration();
            long diff = expirationDate.getTime() - System.currentTimeMillis();
            return diff > 0 ? diff : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
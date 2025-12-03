package com.example.dvdmangement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    // ⚠️ 과제용이라 그냥 하드코딩. 실제 서비스면 환경변수/설정파일로 빼야 함
    private static final String SECRET_KEY = "my-super-secret-key-for-dvd-project";

    // 토큰 유효시간 (예: 1시간)
    private static final long EXPIRATION_MS = 1000L * 60 * 60;

    // 토큰 생성
    public static String createToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 토큰에서 username 추출 (검증 포함)
    public static String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}

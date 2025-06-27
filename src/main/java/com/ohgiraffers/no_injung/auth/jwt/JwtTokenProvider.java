package com.ohgiraffers.no_injung.auth.jwt;

import com.ohgiraffers.no_injung.user.entity.Role;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    private final UserRepository userRepository;
    private final long tokenValidTime = 1000L * 60 * 60; // 1시간

    /**
     * 사용자 정보로 JWT 토큰 생성 (Role 정보 포함)
     */
    public String createToken(String email) {
        // 사용자 정보 조회하여 Role 가져오기
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
        
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim("role", user.getRole().name()) // Role 정보 추가
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰에서 Role 정보 추출
     */
    public String getRole(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            String role = claims.get("role", String.class);
            return role != null ? role : "USER"; // 기본값 USER
        } catch (Exception e) {
            return "USER"; // 오류 시 기본값
        }
    }

    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        String role = getRole(token);
        UserDetails userDetails = new CustomUserDetails(email, role);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            return !Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }

    public long getExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime() - System.currentTimeMillis();
    }
}

package com.ohgiraffers.no_injung.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//요청 시 JWT 파싱하여 사용자 인증
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = tokenProvider.resolveToken(request);

        if (token != null && tokenProvider.validateToken(token)) {
            //Redis에 토큰이 로그아웃 처리 되어 있는지 확인
            String isLogout = redisTemplate.opsForValue().get(token);
            if (isLogout != null) {
                // 로그아웃된 토큰 → 인증 처리하지 않음
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("로그아웃된 토큰입니다.");
                return;
            }
            // ✅ 유효한 토큰이고, 블랙리스트 아님 → 인증 처리
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}

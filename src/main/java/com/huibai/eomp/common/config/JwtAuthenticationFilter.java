package com.huibai.eomp.common.config;

import com.huibai.eomp.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        try {
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);

                if (jwtUtils.validateToken(token)) {
                    String username = jwtUtils.getUsernameFromToken(token);
                    // 【关键点 1】从 Token 获取 userId
                    Integer userId = jwtUtils.getUserIdFromToken(token);

                    // 构建 Spring Security 认证信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 【关键点 2】存入自定义上下文，供业务层 UserContext.getUserId() 使用
                    if (userId != null) {
                        com.huibai.eomp.common.security.UserContext.setUserId(userId);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            // 【关键点 3】非常重要：请求完成后必须移除，否则 ThreadLocal 在线程池复用时会导致数据错乱
            com.huibai.eomp.common.security.UserContext.remove();
        }
    }
}
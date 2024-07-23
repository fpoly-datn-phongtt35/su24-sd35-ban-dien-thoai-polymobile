package com.poly.polystore.config.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import com.poly.polystore.config.SecurityConfig;
import com.poly.polystore.core.common.login.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        // Kiểm tra xem đường dẫn có nằm trong danh sách không yêu cầu xác thực
        if (Arrays.stream(SecurityConfig.unAuthURL).anyMatch(pattern -> antPathMatcher.match(pattern, requestUri))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        final String jwt;
        final String email;
        if(authorization == null) {
            final Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                        .filter(cookie -> "Authorization".equals(cookie.getName()))
                        .findFirst();
                if (jwtCookie.isPresent()) {
                    authorization ="Bearer "+ jwtCookie.get().getValue();
                }
            }
        }
        if (authorization==null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Không có header/cookie cho đi tiếp nhưng không authorize
        System.out.println("Request URL: " + ((HttpServletRequest) request).getRequestURI());


        jwt = authorization.substring(7);
        log.debug("JWT - {}", jwt.toString());
        if(jwtService.isTokenExpired(jwt)){
            log.debug("Token is expired");
            filterChain.doFilter(request, response);
            return;
        }
        email = jwtService.extractEmail(jwt);
        if (email!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails =userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.debug("User - {}", userDetails);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
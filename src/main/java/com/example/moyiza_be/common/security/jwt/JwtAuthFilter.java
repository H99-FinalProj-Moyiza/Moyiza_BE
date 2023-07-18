package com.example.moyiza_be.common.security.jwt;

import com.example.moyiza_be.common.security.SecurityExceptionDto;
import com.example.moyiza_be.domain.user.entity.User;
import com.example.moyiza_be.domain.user.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Interpreting and extracting JWT tokens
        String access_token = jwtUtil.resolveToken(request, JwtUtil.ACCESS_TOKEN);
        String refresh_token = CookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        // Validate the token if it exists, and throw an exception if it's invalid
//        log.info("JwtAuthFilter activated");
        if(access_token == null){
            filterChain.doFilter(request, response);
        } else {
            if (jwtUtil.validateToken(access_token)) {
//                jwtUtil.checkTokenClaims(access_token);
                setAuthentication(jwtUtil.getUserInfoFromToken(access_token));
            } else if (refresh_token != null && jwtUtil.refreshTokenValid(refresh_token)) {
                //Get username with Refresh token
                String userEmail = jwtUtil.getUserInfoFromToken(refresh_token);
                //Get user information by username
                User user = validationUtil.findUserByEmail(userEmail);
                //Issue a new ACCESS TOKEN
                String newAccessToken = jwtUtil.createToken(user, "Access");
                //Add an ACCESS TOKEN to the header
                response.setHeader("ACCESS_TOKEN", newAccessToken);
                setAuthentication(userEmail);
            } else if (refresh_token == null) {
                jwtExceptionHandler(response, "The AccessToken has expired.", HttpStatus.BAD_REQUEST.value());
                return;
            } else {
                jwtExceptionHandler(response, "Your RefreshToken has expired. Please sign in again.", HttpStatus.BAD_REQUEST.value());
                return;
            }
            //Continue the filter chain by forwarding requests and responses to this filter
            filterChain.doFilter(request, response);
        }
    }

    // Create an authentication object and set it to a SecurityContext
    public void setAuthentication(String userEmail) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(userEmail);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // Setting up responses for JWT exception handling
    public void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json; charset=utf8"); //Troubleshooting broken Korean
        try {
            // Convert exception information to JSON and write it to the response
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, message));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
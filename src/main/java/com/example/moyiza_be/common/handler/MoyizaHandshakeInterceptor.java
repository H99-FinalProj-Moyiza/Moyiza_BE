package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.common.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MoyizaHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("handshakeInterceptor activated");
        System.out.println("request = " + request);
        System.out.println("request.getHeaders() = " + request.getHeaders());
        if(!(request instanceof HttpServletRequest)){
            System.out.println("ServerHttpRequest is not instance of ServletRequest....");
        }
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        System.out.println("request.getHeaders().get(\"ACCESS_TOKEN\"); = " + request.getHeaders().get("ACCESS_TOKEN"));
        String token = jwtUtil.resolveToken(servletRequest, "ACCESS_TOKEN");
        Claims claims = jwtUtil.getClaimsFromToken(token);
        attributes.put("userId", Long.valueOf(claims.get("userId").toString()));
        attributes.put("nickName", claims.get("nickName").toString());
        attributes.put("profileUrl", claims.get("profileUrl").toString());


        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}

package com.example.moyiza_be.common.oauth2.handler;

import com.example.moyiza_be.common.oauth2.CustomOAuth2User;
import com.example.moyiza_be.common.oauth2.Role;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        log.info("OAuth2 Login Success!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            User findUser = userRepository.findByEmail(oAuth2User.getEmail()).orElseThrow(
                    () -> new NoSuchElementException("Member doesn't exist."));
            jwtUtil.createAndSetToken(response, findUser);
            if(oAuth2User.getRole() == Role.GUEST) {
                // If the user's Role is GUEST, redirect them to the signup page because they don't have enough information.
                response.sendRedirect("http://moyiza.s3-website.ap-northeast-2.amazonaws.com/signup/social");
            } else {
                response.sendRedirect("http://moyiza.s3-website.ap-northeast-2.amazonaws.com/");
            }

        } catch (Exception e) {
            throw e;
        }
    }
}

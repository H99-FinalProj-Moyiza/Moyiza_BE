package com.example.moyiza_be.common.oauth2.handler;

import com.example.moyiza_be.common.enums.UserRoleEnum;
import com.example.moyiza_be.common.oauth2.CustomOAuth2User;
import com.example.moyiza_be.common.oauth2.Role;
import com.example.moyiza_be.common.security.jwt.JwtTokenDto;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshToken;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshTokenRepository;
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
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if(oAuth2User.getRole() == Role.GUEST) {

                JwtTokenDto tokenDto = jwtUtil.createAllToken(oAuth2User.getEmail());
                Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(oAuth2User.getEmail());
                if (refreshToken.isPresent()) {
                    refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
                } else {
                    RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), oAuth2User.getEmail());
                    refreshTokenRepository.save(newToken);
                }
                setHeader(response, tokenDto);
                response.sendRedirect("/user/signup");

//                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
//                                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
//                findUser.authorizeUser();
            } else {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        JwtTokenDto tokenDto = jwtUtil.createAllToken(oAuth2User.getEmail());
        setHeader(response, tokenDto);

//        String accessToken = jwtUtil.createAccessToken(oAuth2User.getEmail());
//        String refreshToken = jwtUtil.createRefreshToken();
//        response.addHeader(jwtUtil.getAccessHeader(), "Bearer " + accessToken);
//        response.addHeader(jwtUtil.getRefreshHeader(), "Bearer " + refreshToken);
//
//        jwtUtil.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        jwtUtil.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }

    private void setHeader(HttpServletResponse response, JwtTokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}

package com.example.moyiza_be.common.security.jwt;

import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.common.security.userDetails.UserDetailsServiceImpl;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshToken;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshTokenRepository;
import com.example.moyiza_be.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final long ACCESS_TIME = Duration.ofMinutes(60).toMillis();
    private static final long REFRESH_TIME = Duration.ofDays(7).toMillis();

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request, String token) {
        String tokenName = token.equals("ACCESS_TOKEN") ? ACCESS_TOKEN : REFRESH_TOKEN;
        String bearerToken = request.getHeader(tokenName);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public Claims getClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public String removePrefix(String bearerToken){
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public JwtTokenDto createAllToken(User user) {
        return new JwtTokenDto(createToken(user, "Access"), createToken(user, "Refresh"));
    }

    public String createToken(User user, String token) {
        Date date = new Date();
        long time;
        if(token.equals("Access")){
            time = ACCESS_TIME;
            return BEARER_PREFIX +
                    Jwts.builder()
                            .setSubject(user.getEmail())
                            .claim("userId", user.getId())
                            .claim("nickName", user.getNickname())
                            .claim("profileUrl", user.getProfileImage())
                            .setExpiration(new Date(date.getTime() + time))
                            .setIssuedAt(date)
                            .signWith(key, signatureAlgorithm)
                            .compact();
        }
        else{
            time = REFRESH_TIME;
            return Jwts.builder()
                            .setSubject(user.getEmail())
                            .setExpiration(new Date(date.getTime() + time))
                            .setIssuedAt(date)
                            .signWith(key, signatureAlgorithm)
                            .compact();
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public String getUserInfoFromToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String userEmail) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    //RefreshToken 검증
    public boolean refreshTokenValid(String token) {
        if (!validateToken(token)) return false;
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(getUserInfoFromToken(token));
        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    public void createAndSetToken(HttpServletResponse response, User user) {
        JwtTokenDto tokenDto = createAllToken(user);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(user.getEmail());
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user.getEmail());
            refreshTokenRepository.save(newToken);
        }
        cookieUtil.addCookie(response, JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
        response.setHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
    }

    public ChatUserPrincipal tokenToChatUserPrincipal(String token){
        Claims claims = getClaimsFromToken(token);
        ChatUserPrincipal userInfo;
        try{
            userInfo = new ChatUserPrincipal(
                    Long.valueOf(claims.get("userId").toString()),
                    claims.get("nickName").toString(),
                    claims.get("profileUrl").toString(),
                    -1L
            );
        } catch(Exception e){
            log.info("채팅 : 토큰에서 유저정보를 가져올 수 없음");
            throw new NullPointerException("chat : 유저정보를 읽을 수 없습니다");
        }
        return userInfo;
    }

}
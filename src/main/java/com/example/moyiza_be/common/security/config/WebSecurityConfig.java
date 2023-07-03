package com.example.moyiza_be.common.security.config;

import com.example.moyiza_be.common.handler.CustomAccessDeniedHandler;
import com.example.moyiza_be.common.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.moyiza_be.common.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.moyiza_be.common.oauth2.service.CustomOAuth2UserService;
import com.example.moyiza_be.common.security.jwt.JwtAuthFilter;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final String MOYIZADOMAIN = "https://mo2za.com";

    private static final String[] PERMIT_URL_ARRAY = {
            "/*",
            "/chat/**",
            "/login",
            "/uploadImg",
            "/signup/**",
            "/check/**",
            "/find/email/**",
            "/oauth2/authorization/**",
            "/enums",
            "/.well-known/acme-challenge/**" //Verify domain ownership for Certbot
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Allow access to resources
        return (web) -> web.ignoring()
//                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.cors().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(PERMIT_URL_ARRAY).permitAll()
                .requestMatchers(HttpMethod.GET, "/club/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/event/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/oneday/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/*").permitAll()
                .anyRequest().hasAnyAuthority("ROLE_USER", "ROLE_ADMIN").and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin(MOYIZADOMAIN);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://moyiza.dev.s3-website.ap-northeast-2.amazonaws.com/");
        config.addAllowedOrigin("https://hohomii.shop");

        config.addExposedHeader(JwtUtil.ACCESS_TOKEN);

        config.addExposedHeader("Set-Cookie");

        config.addAllowedMethod("*");

        config.addAllowedHeader("*");

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

}

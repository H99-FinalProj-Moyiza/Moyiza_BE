//package com.example.moyiza_be.config;
//
//import com.example.moyiza_be.common.utils.JwtUtil;
//import jakarta.servlet.DispatcherType;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity // (debug = true) // 스프링 Security 지원을 가능하게 함
////@EnableGlobalMethodSecurity(securedEnabled = true)
//@RequiredArgsConstructor
//public class WebSecurityConfig{
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
////    private final CorsConfig corsConfig;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public WebSecurityCustomizer webSecurityCustomizer() {
////        // h2-console 사용 및 resources 접근 허용 설정
////        return (web) -> web.ignoring()
//////                .requestMatchers(PathRequest.toH2Console())
////                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
////    }
//
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration config = new CorsConfiguration();
////
////        config.setAllowedOrigins(List.of("http://localhost:3000"));
////        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
////        config.setAllowedHeaders(List.of("Cookie","Access-Control-Request-Method","Access-Control-Request-Headers"));
////        config.setExposedHeaders(List.of(JwtUtil.AUTHORIZATION_HEADER));
////        config.setAllowCredentials(true);
////
////        //URL별 설정
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", config);
////        return source;
////    }
//
//    @Bean
//    UserDetailsServiceImpl userDetailsServiceimpl(){
//        return new UserDetailsServiceImpl(userRepository);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // CSRF 설정
////        http.cors().configurationSource(corsConfigurationSource());
//        //세션 사용 안함
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.cors();
//
//        http.csrf().disable().authorizeHttpRequests()
////                .antMatchers(HttpMethod.GET, "/api/article").permitAll()
////                .antMatchers("/h2-console/**").permitAll()
////                .antMatchers("/css/**").permitAll()
////                .antMatchers("/js/**").permitAll()
////                .antMatchers("/images/**").permitAll()
////                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                .requestMatchers("/api/user/**").permitAll()
//                .requestMatchers("/api/stay/**").permitAll()
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .requestMatchers(HttpMethod.GET,"api/stay").permitAll()
//                .requestMatchers(HttpMethod.GET, "api/stay/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsServiceimpl()), UsernamePasswordAuthenticationFilter.class);
//        // 로그인 사용
////        http.formLogin().loginPage("/api/user/login-page")
////                .successHandler(new LoginSuccessHandler(jwtUtil))
////                .permitAll();
////        http.formLogin().loginProcessingUrl("/api/user/login");
//
//        return http.build();
//    }
//}
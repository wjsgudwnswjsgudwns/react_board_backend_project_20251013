package com.jhj.home.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity // 모든 요청이 url이 스프링 시큐리티의 제어를 받도록하는 annotation
public class SecurityConfig {
	
	 // 비밀번호 암호화용 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 필터 체인 설정 (Spring Boot 3.x / Security 6.x 방식)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (API 서버일 경우)
            .csrf(csrf -> csrf.disable()) // csrf 인증을 비활성화 -> react 같은 프론트앤드+백앤드 구조에서는 불필요
            .cors(Customizer.withDefaults()) // cors -> 활성화

            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/signup", "/api/auth/login", "/api/board", "/api/board/**").permitAll() // 공개 페이지
                .anyRequest().authenticated() // 나머지는 인증 필요
            )

            // 로그인 설정
            .formLogin(login -> login // 아이디와 비밀번호 확인은 여기서 -> 로그인이 되면 세션 생성
                .loginProcessingUrl("/api/auth/login") // 커스텀 로그인 페이지 경로
                .usernameParameter("username")
                .passwordParameter("password")
                // 로그인 성공시 -> ok -> 200
                .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK)) // api로 로그인 요청시 추가 사항
                // 로그인 실패시 -> fail -> 401
                .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)) // api로 로그인 요청시 추가 사항
                
            )

            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
                
            );
        

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React 개발 서버
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 쿠키, 세션 허용 시 필요

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
   

}



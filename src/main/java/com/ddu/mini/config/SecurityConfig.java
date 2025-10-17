package com.ddu.mini.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ddu.mini.repository.MemberRepository;
import com.ddu.mini.service.CustomUserDetailService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // csrf 인증을 비활성화 -> React, Vue.js 같은 프론트 + 백 엔드 구조에서는 불필요
		.cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors => 활성화
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/index.html", "/static/**").permitAll()
				.requestMatchers("/api/member/signup", "/api/member/login", "/api/post", "/api/post/**","/api/comment","/api/comment/**")
				.permitAll().requestMatchers("/", "/signup","/login" ,"/post", "/post/**","/write" ).permitAll()
				.anyRequest().authenticated()
				)
				// 아이디 && 비밀번호를 security 에서 확인 후 session 까지 생성해줌 
				.formLogin(login -> login.loginProcessingUrl("/api/member/login") // 로그인 요청 URL
						.usernameParameter("email")
						.passwordParameter("password")
						// 로그인 성공시 -> OK -> 200 
						.successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
						// 로그인 실패시 -> FAIL -> 401
						.failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
				)
				.logout(logout -> logout.logoutUrl("/api/member/logout") // 로그아웃 요청 URL
						// 로그아웃 성공시 -> OK -> 200 
						.logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
				);
		
		return http.build();
	}
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	//프론트엔드 리액트에서 요청하는 주소 허용
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
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(customUserDetailService)
				.passwordEncoder(encoder())
				.and()
				.build();
}
}

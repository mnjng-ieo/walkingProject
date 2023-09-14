package com.walk.aroundyou.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// "/signup":회원가입하기 버튼의 액션(/view/join)
	// .defaultSuccessUrl("/view/dashboard",true)
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable())
			.cors((cors) -> cors.disable())
			.authorizeHttpRequests(request -> request
					.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
					.requestMatchers("/").permitAll()
					.requestMatchers("/status", "/images/**", "/css/**", "/js/**", 
									"/login", "/main", "/signup/**", 
									"/login/idlookup", "/login/pwdlookup/**").permitAll()
					.requestMatchers("/guest/**").permitAll()
					//.requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
					//.requestMatchers("/admin/**").hasRole("ADMIN")
					.requestMatchers("/**").permitAll()
					//.requestMatchers("/**").hasAnyRole("USER")
					//.anyRequest().authenticated()
					.anyRequest().permitAll()
					);
        http.formLogin((formLogin) ->
		        formLogin.permitAll()); // 기본 로그인 페이지
		http.logout((logout) -> 
		     logout.permitAll());      // 로그아웃 기본설정 (/logout으로 인증해제)
					

		return http.build();
	}

}

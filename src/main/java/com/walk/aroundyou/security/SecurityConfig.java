package com.walk.aroundyou.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.walk.aroundyou.domainenum.UserRole;

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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable()).cors((cors) -> cors.disable())
		
				.authorizeHttpRequests(request -> request.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/status", "/images/**", "/upload-images/**", "/css/**", "/js/**", "/login", "/", "/signup/**", "/login/idlookup", "/login/pwdlookup/**")
						.permitAll()
						.requestMatchers("/**").permitAll() // 나중에 삭제
						.requestMatchers("/**").hasAuthority(UserRole.USER.getRoleName()) // user만 가능한 기능 uri 넣기
						.requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.getRoleName())
						.anyRequest().authenticated())
				
				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/check").usernameParameter("userId")
						.passwordParameter("userPwd").defaultSuccessUrl("/", true) // ("/main")은 임시, 나중에는 ("/")로 변경
						.failureHandler(new AuthenticationFailureHandler() {
					            @Override
					            public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException, ServletException {
					            	
					            	String errorMessage = "아이디나 비밀번호가 맞지 않습니다!";
					            	
					            	req.setAttribute("errorMessage", errorMessage);
					            	res.sendRedirect("/login?error=true");
					            }
					        })
						.permitAll());

		
		http.logout((logout) -> logout.logoutUrl("/logout") // default
				.logoutSuccessUrl("/") // ("/main")은 임시, 나중에는 ("/")로 변경
				// 사용자의 세션을 무효화
				.addLogoutHandler(new LogoutHandler() {

					@Override
					public void logout(HttpServletRequest req, HttpServletResponse res,
							Authentication authentication) {
						HttpSession session = req.getSession();
						session.invalidate();
					}
				}).permitAll());
		
		
		http.exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthenticationEntryPoint() {
			
				@Override
				public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
					// 인증되지 않은 사용자가 페이지에 올 경우
					// 로그인 하시면 way의 더 많은 기능을 사용하실 수 있습니다!
					res.sendRedirect("/error");
				}
			})
			.accessDeniedHandler(new AccessDeniedHandler() {
				
				@Override
				public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException) throws IOException, ServletException {
					
					// 유저가 관리자의 권한 페이지를 사용하려고 하는 경우
					// 관리자만 권한이 있는 페이지입니다!
					res.sendRedirect("/error");
				}
			}));

		return http.build();
	}

}
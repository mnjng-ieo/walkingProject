package com.walk.aroundyou.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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

	// "/signup":회원가입하기 버튼의 액션(/view/join)
	// .defaultSuccessUrl("/view/dashboard",true)
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable()).cors((cors) -> cors.disable())
		
				.authorizeHttpRequests(request -> request.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/status", "/images/**", "/css/**", "/js/**", "/login", "/main", "/signup/**", "/login/idlookup", "/login/pwdlookup/**")
						.permitAll()
						.requestMatchers("/**").permitAll()
						//.requestMatchers("/**").hasAuthority(UserRole.USER.getRoleName())
						.requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.getRoleName())
						//.anyRequest().authenticated())
				.anyRequest().authenticated())
				
				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/check").usernameParameter("userId")
						.passwordParameter("userPwd").defaultSuccessUrl("/", true) // ("/main")은 임시, 나중에는 ("/")로 변경

						.permitAll());

		http.logout((logout) -> logout.logoutUrl("/logout") // default
				.logoutSuccessUrl("/main") // ("/main")은 임시, 나중에는 ("/")로 변경
				// 사용자의 세션을 무효화
				.addLogoutHandler(new LogoutHandler() {

					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) {
						HttpSession session = request.getSession();
						session.invalidate();
					}
				}).permitAll());

		return http.build();
	}
	

}

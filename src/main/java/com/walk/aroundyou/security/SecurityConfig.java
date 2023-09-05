package com.walk.aroundyou.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.walk.aroundyou.service.UserDetailService;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {
	
	// 시큐리티 처리를 위한 서비스 클래스 의존성 주입하기
	private final UserDetailService userService;
	
	@Autowired
	public SecurityConfig(UserDetailService userService) {
		this.userService = userService;
	}
	
	
	// 특정 HTTP 요청에 대한 웹 기반 보안 구성
	@SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// Spring Security 버전 6.1 이상에서 and() 메서드가 더 이상 사용되지 않기 때문에 이 방식으로 구현
        http.csrf((csrf) -> csrf.disable())
        	.cors((cors) -> cors.disable())
        	// 인증, 인가, 권한 설정
            .authorizeHttpRequests(request -> request
            	// .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll(): DispatcherType.FORWARD로 전달되는 요청에 대해 접근을 허용합니다. 예를 들어, Forward 되는 요청에 대한 접근을 허용하고자 할 때 사용됩니다.
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/guest/**").permitAll()
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
            );
        // 폼 기반 로그인 설정
        // http.formLogin((formLogin) -> formLogin : formLogin 객체를 받아 설정을 수행합니다. 즉, formLogin 객체를 생성한 후 이를 사용하여 폼 로그인 설정을 구성
        http.formLogin((formLogin) -> formLogin
			.loginPage("/loginForm") 			// default : /login
			.loginProcessingUrl("/check")
			.usernameParameter("userId")	// default : userId
			.passwordParameter("userPwd") 	// default : userPwd
			.defaultSuccessUrl("/main")
			.failureUrl("/loginerror")
			.permitAll());
        
        // 로그아웃 설정
        http.logout((logout) -> logout
        	.logoutUrl("/logout") // default
        	.logoutSuccessUrl("/loginForm")
        	.permitAll());
        
        
        return http.build();
    }
	
	
	
	// 사용자 정보를 메모리에 저장
	@Bean
	public UserDetailsService users() {
		UserDetails user = User.builder()
				.username("user")
				.password(passwordEncoder().encode("1234"))
				  // ROLE_USER 에서 ROLE_는 자동으로 붙는다.
				.build();

		// 메모리에 사용자 정보를 담는다.
		return new InMemoryUserDetailsManager(user);
	}
	
    // passwordEncoder()
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return  PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
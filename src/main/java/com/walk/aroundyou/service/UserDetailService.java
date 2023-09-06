package com.walk.aroundyou.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domain.role.StateId;
import com.walk.aroundyou.domain.role.UserRole;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

// UserService에 passwordEncoder 의존성 주입
// SecurityConfig에 UserService 의존성 주입
// passwordEncoder는 SecurityConfig 메서드
// => 의존성 순환 문제 발생으로 passwordEncoder 부분은 
@Service
public class UserDetailService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Autowired
	public UserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	///// 사용자 정보를 가져오는 로직
	/// loadUserByUsername : Spring Security에서 사용자 인증과 권한 부여를 처리하는 메서드인 
	/// loadUserByUsername(String userId) : userId를 기반으로 사용제 세부 정보를 로드
	// UserDetailsService에 정의된 메서드를 오버라이드
	@Override
	// userId를 매개변수로 받고 사용자의 세부 정보(아이디, 비밀번호, 권한 등)을 나타내는 UserDetails객체를 반환
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
		// .orElseThrow(() -> new IllegalArgumentException((userId))); : 사용자가 리포지토리에서 찾을 수 없는 경우 처리
		// Optional<User>객체를 반환했는데 비어있는 경우 userId를 메시지로 하는 IllegalArgumentException 예외를 던짐(throw)
		 return userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException((userId))); 
	} 
		 
	private class UserDeatils extends User implements UserDetails {	 
		UserDeatils(User user) {
			setUserId(user.getUserId());
			setUserPwd(user.getUserPwd());
			setUserName(user.getUsername());
			setUserTelNumber(user.getUserTelNumber());
			setUserEmail(user.getUserEmail());
			setUserImg(user.getUserImg());
			setUserDescription(user.getUserDescription());
		}
	}
	
}


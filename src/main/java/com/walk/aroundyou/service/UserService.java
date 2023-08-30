package com.walk.aroundyou.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.dto.UpdateUserRequest;
import com.walk.aroundyou.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired	
	UpdateUserRequest updateUserRequest;	// DTO 클래스 : 서비스와 상호작용
	
	
	
	// 멤버 메소드
	
	// JPA를 사용하면 DTO, VO 변환 시 
	// modelMapper의 map()메소드를 사용하지 않아도 된다.
	
	public User register(User user) {
		// User : VO, UpdateUserRequest : DTO
		
		return userRepository.register(user);
	}
	
	public User save(UpdateUserRequest updateUserRequest) {
		return userRepository.save(updateUserRequest);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	// 리포지토리는 DB로부터 데이터(vo)를 가져옴
	// 서비스는 리포지토리로부터 데이터를 잘못받은 경우, 서비스는 컨트롤러에게 넘겨준다
	
	public Optional<User> findByUserId(String userId) {
		
		return userRepository.findByUserId(userId);
		
	}
	
	public void deleteByUserId(String userId) {
		
		userRepository.deleteByUserId(userId);
	}
	
	
	
	
	
	}
	
	
	
	
	



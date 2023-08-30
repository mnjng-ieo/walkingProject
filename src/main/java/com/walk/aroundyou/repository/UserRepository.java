package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.dto.UpdateUserRequest;


public interface UserRepository extends JpaRepository<User ,String> {
	
	
	
	// 1. user entity를 등록 하기 (최초에 등록할 시)
	User register(User user);
    
	// 1-1. user Entity를 수정 하기 (최초 등록 이후에, 업데이트 할 시)
	User save(UpdateUserRequest updateUserRequest);
	
	
    // 2. Id로 유저 검색하기
    // (리턴값이 null일수도 있으므로, NPE를 대비하여 Optional 타입으로 지정함)
    Optional<User> findByUserId(String userId);
    
    // 3. Name으로 유저 검색하기
    Optional<User> findByUserName(String userName);
    
    // 4. Nickname으로 유저 검색하기
    Optional<User> findByUserNickname(String userNickname);
    
    // 5. user entity의 모든 항목을 반환하기
    List<User> findAll();
    
    // 6. Id로 검색한 엔터티 삭제하기
    void deleteByUserId(String userId);


    
    
/// 회원 권한에서도 사용 가능한 메소드
    // board 리포지토리 관련 메소드들
    
	
	// Nickname 으로 유저(유저목록: 동일한 닉네임 존재 가능함) 검색
	// Optional<User> findByUserNickname(String userNickname);
	
	
	// 회원정보 수정 가능한 목록 반환하기 (패스워드를 입력했을 때만)
	// 회원이 본인 고유의 id(userId), userPwd로 로그인해서 
	// 회원정보 수정을 위해 다시 한 번 userPwd를 입력해야만 
	// 회원정보 수정이 가능하다. => findByIdAndPwd()
	
    
	// User 테이블의 멤버변수 중 
	// userJoinDate, userUpdateDate, tateId, userRole 는 반환하지 않는다. 
	// findById(String userId, String userPwd);
	
	
	
	
}

package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walk.aroundyou.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	// 1. 회원가입 시 User엔티티 전부 가져오기
	User save(User user);
	
	
	// 2. 아이디로 유저 찾기 (로그인할 때 id로 정보 받기, pw변경 및 찾기, 회원 정보 보기에서 아이디로 회원 찾기, 회원 삭제할 때 아이디로 조회해 삭제 | 관리자가 회원 검색)
	Optional<User> findByUserId(String userId);
	
	
	
	// 3. Id찾기 : 이름(userName)과 이메일(userEmail) 정보를 입력 받아 아이디 찾기
	Optional<User> findByUserNameAndUserEmail(String userName, String userEmail);

	
	
	// 4. Nickname으로 유저 검색하기 - 유저 닉네임은 중복이 가능 | USER가 닉네임 검색해서 내가 쓴 글, 댓글 검색
	// LIKE 연산자를 사용할 때, 필드 이름을 직접 사용하지 않아도 됨.
	// 파라미터에 전달되는 값을 사용할 때, 필드 이름을 사용하는 것이 아니라 파라미터 이름을 사용하여 매칭시키기 때문
	// '%'연산자 jpql에서 사용하지 않음 -> 서비스 클래스에 추가
    @Query("SELECT u FROM User u WHERE u.userNickname LIKE :userNickname")
    List<User> searchByUserNickname(@Param(value = "userNickname")String userNickname);

    
    
	// 5. user entity의 모든 항목을 반환하기
	List<User> findAll();
	
	
	// 6. Id로 검색한 엔터티 삭제하기
	void deleteByUserId(String userId);

}

package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.IUserResponse;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<Member, String> {
	
	// 1. 회원가입 시 User엔티티 전부 가져오기
	@SuppressWarnings("unchecked")
	@Modifying
	@Transactional
	Member save(Member member);
	
	
	// 2. 아이디로 유저 찾기 (로그인할 때 id로 정보 받기, pw변경 및 찾기, 회원 정보 보기에서 아이디로 회원 찾기, 회원 삭제할 때 아이디로 조회해 삭제 | 관리자가 회원 검색)
	Optional<Member> findByUserId(String userId);
	
	
	// 3. Id찾기 : 이름(userName)과 이메일(userEmail) 정보를 입력 받아 아이디 찾기
	Optional<Member> findByUserNameAndUserEmail(String userName, String userEmail);

	
    
	// 4. 관리자페이지에서 출력할 유저 정보
	   @Query(value ="""
		         SELECT 
					user_id as userId
					, user_name as userName
					, user_nickname as userNickname
					, user_tel_number as userTelNumber
					, user_email as userEmail
					, user_description as userDescription	
					, user_join_date as userJoinDate
					, user_update_date as userUpdateDate
				    , user_role as role
				    , state_id as stateId
		         FROM user u
		         GROUP BY user_join_date 
	   			 ORDER BY user_join_date desc
		               """ 
		         , nativeQuery = true)
	Page<IUserResponse> findAllUsers(Pageable pageable);
	
    
	// 5. user entity의 모든 항목을 반환하기
	List<Member> findAll();
	
	
	// 6. Id로 검색한 엔터티 삭제하기
	@Modifying
	@Transactional
	void deleteByUserId(String userId);
	
	
	// 7. userEmail로 비밀번호 보내기
	Optional<Member> findByUserEmail(String userEmail);

}
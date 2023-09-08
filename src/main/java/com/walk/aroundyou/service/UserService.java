package com.walk.aroundyou.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.role.StateId;
import com.walk.aroundyou.domain.role.UserRole;
import com.walk.aroundyou.dto.UserRequest;
import com.walk.aroundyou.repository.UserRepository;


@Service
public class UserService{

	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	
	// 회원가입
	public String join(String userid, String pw) {
		
		UserRequest request = new UserRequest();
        Member member = request.toEntity();
        
        validateDuplicateMember(member);
        userRepository.save(member);

        return member.getUserId();
    }
	// 중복 아이디 체크
    private void validateDuplicateMember(Member member) {
    	userRepository.findByUserId(member.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
	
	
	// 1. 회원가입 시 User엔티티 전부 가져오기
	// 회원가입, User 엔티티들 변경 후에(update) save로 저장
	public Member saveUser(Member member) {
		
		return userRepository.save(member);
	}
	
	/////////////////////// 아이디 중복 체크
	public boolean isUserIdDuplicate(String userId) {
	Optional<Member> member = userRepository.findByUserId(userId);
	return member.isPresent();
	}

	///// 마이페이지에서 아이디 받아와서 정보변경하면 업데이트해주기
	public Member updateMypage(String userId, String userNickname, String userImg, String userDescription) {
		
		// id로 user 확인
		Optional<Member> userOptional = userRepository.findByUserId(userId);
		Member member = userOptional.get();
		
		// 유저가 있다면
		if(member != null) {
			
			// 유저 정보 변경
			member.setUserNickname(userNickname);
			member.setUserImg(userImg);
			member.setUserDescription(userDescription);
			
			// 유저 정보 저장
			return userRepository.save(member);
		}
		// user == null
		return null;
	}
	
	///// 회원 정보 보기에서 정보 업데이트(유저 페이지)
	public Member updateUserInfo(String userId, String userName, String userNickname, String userTelnumber, String userEmail) {
		
		// id로 user 확인
	    Optional<Member> userOptional = userRepository.findByUserId(userId);

	    if (userOptional.isPresent()) {
	        Member member = userOptional.get();

	        // 유저 정보 변경
	        member.setUserName(userName);
	        member.setUserNickname(userNickname);
	        member.setUserTelNumber(userTelnumber);
	        member.setUserEmail(userEmail);

	        // 변경된 유저 정보 저장
	        return userRepository.save(member);
	    }

	    // user가 존재하지 않을 경우 null 반환
	    return null;
	}


	
	///// 2. 아이디 검색
	// 아이디를 통해 회원 정보 찾기에 이용 등
	public Optional<Member> findByUserId(String userId) {
		
		return userRepository.findByUserId(userId);
	}
	
	
	///// 5. user entity의 모든 항목을 반환하기
	public List<Member> findAll() {
		return userRepository.findAll();
	}
	
	
	/////////////////////// 아이디 찾기
	public String searchByUserId(String userName, String userEmail) {
		
		// 이름과 이메일을 입력 받아 이 둘을 기준으로 
		Optional<Member> member = userRepository.findByUserNameAndUserEmail(userName, userEmail);
		
		if (member.isPresent()) {
			String foundUserId = member.get().getUserId();
			return "찾으시는 아이디는 \"" + foundUserId + "\" 입니다.";
		} else {
			return "입력하신 정보와 일치하는 아이디가 없습니다.";
		}
	}
	

	///////////////////// 6. 유저 삭제(탈퇴, 강제 삭제)
	// 특정아이디 삭제 -> 탈퇴
	public void deleteByUserId(String userId, String currentPwd) {
		
		// id를 기준으로 사용자 정보(비밀번호)를 가져옴
		Optional<Member> userOptional = userRepository.findByUserId(userId);
		Member member = userOptional.get();
		
		// 비밀번호 한 번 확인 작업 거친 후 탈퇴
		// (암호화된)디비에 있는 비번과 현재 입력한 비번이 같을 경우
		if (passwordEncoder.matches(currentPwd, member.getUserPwd())) {
        userRepository.deleteByUserId(userId);
		}
    }
	// 관리자가 삭제(강제 탈퇴) - 실제로 삭제하는 곳은 서비스 클래스
	public void deleteByAdmin(String userId, UserRole userRole) {
		if (userRole == UserRole.ADMIN) {
			userRepository.deleteByUserId(userId);
		}
	}
	
	
	/////////////////////// 비밀번호 변경
	// 비밀번호 변경할 때 로그인 된 아이디를 기준으로 비밀번호 입력하고 맞으면 
	public boolean changePwd(String userId, String currentPwd, String newPwd, String newPwdConfirm) {
		
		// 아이디 기준이므로 먼저 아이디 조회
		Optional<Member> userOptional = userRepository.findByUserId(userId);
		// Optional<User>객체는 실제 'User'객체를 감싸고 있는 래퍼 클래스이다. 
		// 엔티티를 가져올 경우, Optional에서 제공하는 get()메서드를 사용하면 되는데 리포지토리 메서드는 가져올 수 없음.
		// 그래서 Optional<User>객체에서 실제 User객체에 접근할 수 있도록 해줌
		Member member = userOptional.get();
		
		// 유저가 있고(생략 가능-이미 로그인 된 상태니까)
		// (암호화된)디비에 있는 비번과 현재 입력한 비번이 같을 경우
		if (member != null
				&& passwordEncoder.matches(currentPwd, member.getUserPwd())) {

			// 새로운 비밀번호를 두 번 입력해서 값이 동일하면
			if (newPwd.equals(newPwdConfirm)) {
				
				// 새로운 비밀번호를 암호화
				String encryptedPassword = passwordEncoder.encode(newPwd);
				
				// 새로운 비밀번호를 변경하기
				member.setUserPwd(encryptedPassword);
				
				// 유저 객체에 업데이트하기
				userRepository.save(member);
				return true; // 변경 성공
			}else {
				return false; // 새로운 비밀번호가 일치하지 않음
			}
		} else {
			return false; // 새로운 비밀번호가 일치하지 않음
		}
	}
	
	
	
	/////////////////////// 회원가입
	public String registerUser(UserRequest dto) {
	  
		// 사용자로부터 입력받은 평문 비밀번호
        String rawPassword = dto.getUserPwd();

        // 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
		Member member = Member.builder()
				.userId(dto.getUserId())
				.userPwd(encodedPassword)
				.userName(dto.getUserName())
				.userNickname(dto.getUserNickname())
				.userTelNumber(dto.getUserTelNumber())
				.userEmail(dto.getUserEmail())
				.userJoinDate(new Timestamp(System.currentTimeMillis()))
				.userUpdateDate(new Timestamp(System.currentTimeMillis()))
				.role(UserRole.USER)
				.stateId(StateId.NORMAL)
				.socialYn(dto.isSocialYn())
				.build();
		
		return userRepository.save(member).getUserId();
	}
	
	
	/////////////////////// 비밀번호 찾기 
	public String searchByUserPwd(String userId) {
		
		// 유저 아이디를 입력 받아 비밀번호 찾기
		Optional<Member> member = userRepository.findByUserId(userId);
		Member user1 = member.get();
		String user2 = user1.getUserPwd();
		
		// 아이디가 있다면
		if (member.isPresent()) {
			
				return "찾으시는 비밀번호는 \"" + user2  + "\" 입니다.";
		}else {
				return "입력하신 아이디와 일치하는 값이 없습니다.";
		}
	}
	
	
	
	/////////////////////// 상태 정보

}


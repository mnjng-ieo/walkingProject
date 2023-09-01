package com.walk.aroundyou.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
// org.springframework.security.core.GrantedAuthority는 Spring Security 프레임워크에서 사용되는 인터페이스
// 이 인터페이스는 사용자에게 부여된 권한을 나타내는 객체를 나타내며, Spring Security에서 권한을 효과적으로 관리하는 데 사용
// 일반적으로 사용자 객체 (예: UserDetails 인터페이스를 구현한 객체)에 GrantedAuthority 객체의 컬렉션을 포함시켜 사용자의 권한을 정의하고, Spring Security는 이 정보를 기반으로 인증 및 권한 검사를 수행함
// 예를 들어, Spring Security를 사용하는 경우, 사용자에게 부여된 권한을 나타내기 위해 GrantedAuthority 객체를 구현하고, 사용자 정보 객체에 이 권한을 할당하게 됨
// 이를 통해 Spring Security가 사용자의 권한을 관리하고 보안 검사를 수행할 수 있음
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domain.role.UserRole;
import com.walk.aroundyou.repository.UserRepository;


@Service
public class UserService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// 시큐리티 암호화 의존성 주입
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public UserDetails loadUserByUsername(String userId) {
		return userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException((userId)));
	}
	
	// 1. 회원가입 시 User엔티티 전부 가져오기
	// 회원가입, User 엔티티들 변경 후에(update) save로 저장
	public User saveUser(User user) {
		
		return userRepository.save(user);
	}

	///// 마이페이지에서 아이디 받아와서 정보변경하면 업데이트해주기
	public User updateMypage(String userId, String userNickname, String userImg, String userDescription) {
		
		// id로 user 확인
		Optional<User> userOptional = userRepository.findByUserId(userId);
		User user = userOptional.get();
		
		// 유저가 있다면
		if(user != null) {
			
			// 유저 정보 변경
			user.setUserNickname(userNickname);
			user.setUserImg(userImg);
			user.setUserDescription(userDescription);
			
			// 유저 정보 저장
			return userRepository.save(user);
		}
		// user == null
		return null;
	}
	
	///// 회원 정보 보기에서 정보 업데이트(유저 페이지)
	public User updateUserInfo(String userId, String userName, String userNickname, String userTelnumber, String userEmail) {
		
		// id로 user 확인
		Optional<User> userOptional = userRepository.findByUserId(userId);
	User user = userOptional.get();
	
		// user 있다면
		if ( user != null ) {
			
			// 유저 정보 변경
			user.setUserName(userName);
			user.setUserNickname(userNickname);
			user.setUserTelNumber(userTelnumber);
			user.setUserEmail(userEmail);
			
			// 유저 정보 저장
			return userRepository.save(user);
		}
		// user == null
		return null;
	}
	
	///// 2. 아이디 검색
	// 아이디를 통해 회원 정보 찾기에 이용 등
	public Optional<User> findByUserId(String userId) {
		
		return userRepository.findByUserId(userId);
	}
	
	
	///// 4. 닉네임 찾기 
	// '%'추가해주기
	public List<User> searchByUserNickname(String userNickname) {
	    String modifiedUserNickname = "%" + userNickname + "%"; // 검색어에 '%' 추가
	    return userRepository.searchByUserNickname(modifiedUserNickname); // 수정된 검색어로 쿼리 호출
	}
	
	
	///// 5. user entity의 모든 항목을 반환하기
	public List<User> findAll() {
		return userRepository.findAll();
	}
		

	///////////////////// 6. 유저 삭제(탈퇴, 강제 삭제)
	// 특정아이디 삭제 -> 탈퇴
	public void deleteUserById(String userId, String currentPwd) {
		
		// id를 기준으로 사용자 정보(비밀번호)를 가져옴
		Optional<User> userOptional = userRepository.findByUserId(userId);
		User user = userOptional.get();
		
		// 비밀번호 한 번 확인 작업 거친 후 탈퇴
		// (암호화된)디비에 있는 비번과 현재 입력한 비번이 같을 경우
		if (passwordEncoder.matches(currentPwd, user.getUserPwd())) {
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
		Optional<User> userOptional = userRepository.findByUserId(userId);
		// Optional<User>객체는 실제 'User'객체를 감싸고 있는 래퍼 클래스이다. 
		// 엔티티를 가져올 경우, Optional에서 제공하는 get()메서드를 사용하면 되는데 리포지토리 메서드는 가져올 수 없음.
		// 그래서 Optional<User>객체에서 실제 User객체에 접근할 수 있도록 해줌
		User user = userOptional.get();
		
		// 유저가 있고(생략 가능-이미 로그인 된 상태니까)
		// (암호화된)디비에 있는 비번과 현재 입력한 비번이 같을 경우
		if (user != null && passwordEncoder.matches(currentPwd, user.getUserPwd())) {

			// 새로운 비밀번호를 두 번 입력해서 값이 동일하면
			if (newPwd.equals(newPwdConfirm)) {
				
				// 새로운 비밀번호를 암호화
				String encryptedPassword = passwordEncoder.encode(newPwd);
				
				// 새로운 비밀번호를 변경하기
				user.setUserPwd(encryptedPassword);
				
				// 유저 객체에 업데이트하기
				userRepository.save(user);
				return true; // 변경 성공
			}
			
		} else {
			return false; // 비번이 일치하지 않음
		}
		return false; // 조회 실패
	}
	
	
	
	/////////////////////// 회원가입
	public User registerUser(User user) {
		
		// 사용자 비밀번호 암호화
		user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
		// 사용자 이메일 암호화
		user.setUserEmail(passwordEncoder.encode(user.getUserEmail()));
		// 사용자 번호 암호화
		user.setUserTelNumber(passwordEncoder.encode(user.getUserTelNumber()));
		
		// 사용자 정보 저장
		return userRepository.save(user);
	}
	
	
	
	/////////////////////// 로그인 
	public boolean login(String userId, String currentPwd) {

		// 아이디를 기준으로 비밀번호 확인해서 로그인하기
		Optional<User> user = userRepository.findByUserId(userId);

		// 아이디가 존재하면
		if (user.isPresent()) {
			
			// PasswordEncoder의 matches : 입력한 평문 비밀번호와 암호화된 비밀번호가 일치하는지 검증하는 역할
			// userPwd: 사용자가 입력한 평문 비밀번호
			// user.get().getUserPwd(): 데이터베이스에 저장된 암호화된 비밀번호
			// 디비에 저장된 비번과 사용자가 입력한 비번이 비번이 일치하면
			if (passwordEncoder.matches(currentPwd, user.get().getUserPwd())) {
				
				return true; // 로그인 성공
			} else {
				return false; // 비밀번호 틀림
			}
		} else {
			return false; // 아이디 없거나 실패
		}
	}
	
	
	
	/////////////////////// 아이디 찾기
	public String searchByUserId(String userName, String userEmail) {
		
		// 이름과 이메일을 입력 받아 이 둘을 기준으로 
		Optional<User> user = userRepository.findByUserNameAndUserEmail(userName, userEmail);
		
		if (user.isPresent()) {
			
			// .encode : Spring Security의 PasswordEncoder 인터페이스를 구현한 클래스(예: BCryptPasswordEncoder)를 사용하여 문자열을 해시화(암호화)하는 역할
			// DB에 저장된 Id부분 암호화해주기
			String encryptedId = passwordEncoder.encode(user.get().getUserId());
			// get() 메서드는 Optional 클래스에서 사용되는 메서드 
			
			return "찾으시는 아이디는 \"" + encryptedId + "\" 입니다.";
		} else {
			return "입력하신 정보와 일치하는 아이디가 없습니다.";
		}
	}
	
	
	
	/////////////////////// 비밀번호 찾기 
	public String searchByUserPwd(String userId) {
		
		// 유저 아이디를 입력 받아 비밀번호 찾기
		Optional<User> user = userRepository.findByUserId(userId);
		
		// 아이디가 있다면
		if (user.isPresent()) {
			
			// DB에 저장된 Pwd부분 암호화해주기
			// encryptedPassword return값으로 가져오기 위해 string으로~
			String encryptedPassword = passwordEncoder.encode(user.get().getUserPwd());
			
            return "찾으시는 비밀번호는 \"" + encryptedPassword  + "\" 입니다.";
            
        } else {
            return "입력하신 아이디가 없습니다.";
        }
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/////////////////////// 상태 정보

}


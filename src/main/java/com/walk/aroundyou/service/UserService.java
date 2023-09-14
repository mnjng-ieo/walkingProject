package com.walk.aroundyou.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domainenum.StateId;
import com.walk.aroundyou.domainenum.UserRole;
import com.walk.aroundyou.dto.UpdateMypageDTO;
import com.walk.aroundyou.dto.UpdateUserpageDTO;
import com.walk.aroundyou.dto.UserPasswordChangeDTO;
import com.walk.aroundyou.dto.UserSignupDTO;
import com.walk.aroundyou.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/////////////////// 회원가입
//	public String join(String userid, String pw) {
//
//		UserSignupDTO request = new UserSignupDTO();
//		Member member = request.toEntity();
//
//		validateDuplicateMember(member);
//		userRepository.save(member);
//
//		return member.getUserId();
//	}
//	///////////////// 중복 아이디 체크
//	private void validateDuplicateMember(Member member) {
//	userRepository.findByUserId(member.getUserId()).ifPresent(m -> {
//	throw new IllegalStateException("이미 존재하는 회원입니다.");
//	});
//	}

	
	
	///////////// 1. 회원가입 시 User엔티티 전부 가져오기
	// 회원가입, User 엔티티들 변경 후에(update) save로 저장
	public Member saveUser(Member member) {

		return userRepository.save(member);
	}
	
	

	/////////////////////// 회원가입
	public String registerUser(UserSignupDTO dto) {

// 사용자로부터 입력받은 평문 비밀번호
		String rawPassword = dto.getUserPwd();

// 비밀번호를 암호화
		String encodedPassword = passwordEncoder.encode(rawPassword);

		Member member = Member.builder().userId(dto.getUserId()).userPwd(encodedPassword).userName(dto.getUserName())
				.userNickname(dto.getUserNickname()).userTelNumber(dto.getUserTelNumber()).userEmail(dto.getUserEmail())
				.userJoinDate(new Timestamp(System.currentTimeMillis()))
				.userUpdateDate(new Timestamp(System.currentTimeMillis())).role(UserRole.USER).stateId(StateId.NORMAL)
				.socialYn(dto.isSocialYn()).build();

		return userRepository.save(member).getUserId();
	}

	////////////// 유효성 검증 validation Errors
	public Map<String, String> validateHandling(Errors errors) {

		// HashMap import 안 될 경우 import java.util.HashMap; 직접 작성해주기
		Map<String, String> validatorResult = new HashMap<>();
		log.info("map<String, String>" + validatorResult);
		for (FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}

	
	
	
	/////////////////////// 아이디 중복 체크
	public boolean isUserIdDuplicate(String userId) {
		Optional<Member> member = userRepository.findByUserId(userId);
		return member.isPresent();
	}

	
	
	
	/////////// 마이페이지에서 아이디 받아와서 정보변경하면 업데이트해주기
	public Member updateMypage(String userId, UpdateMypageDTO dto) {

		// id로 user 확인
		Optional<Member> userOptional = userRepository.findByUserId(userId);
		Member member = userOptional.get();

		// 유저가 있다면
		if (userOptional.isPresent()) {

			// 유저 정보 변경
			member.setUserId(dto.getUserId());
			member.setUserNickname(dto.getUserNickname());
			member.setUserDescription(dto.getUserDescription());

			// 유저 정보 저장
			return userRepository.save(member);
		}
		// user == null
		return null;
	}

	
	
	
	///////// 회원 정보 보기에서 정보 업데이트(유저 페이지)
	public Member updateUserInfo(UpdateUserpageDTO dto) {

		// id로 user 확인
		Optional<Member> userOptional = userRepository.findByUserId(dto.getUserId());

		// user 있다면
		if (userOptional.isPresent()) {
			Member member = userOptional.get();

			// 유저 정보 변경
			member.setUserId(dto.getUserId());
			member.setUserName(dto.getUserName());
			member.setUserNickname(dto.getUserNickname());
			member.setUserTelNumber(dto.getUserTelNumber());
			member.setUserEmail(dto.getUserEmail());
			member.setUserJoinDate(dto.getUserJoinDate());

			// 유저 정보 저장
			return userRepository.save(member);
		}
		// user == null
		return null;
	}

	
	
	
	
	//////////// 2. 아이디 검색
	// 아이디를 통해 회원 정보 찾기에 이용 등
	public Optional<Member> findByUserId(String userId) {

		return userRepository.findByUserId(userId);
	}
	
	
	

	/////////// 5. user entity의 모든 항목을 반환하기
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
	@Transactional
	public boolean deleteByUserId(String userId, String checkPwd) {

		// id를 기준으로 사용자 정보(비밀번호)를 가져옴
		Optional<Member> userOptional = userRepository.findByUserId(userId);
		if (userOptional.isPresent()) {
			
			Member member = userOptional.get();
			// 비밀번호 한 번 확인 작업 거친 후 탈퇴
			// (암호화된)디비에 있는 비번과 현재 입력한 비번이 같을 경우
			if (passwordEncoder.matches(checkPwd, member.getUserPwd())) {
				userRepository.deleteByUserId(member.getUserId());
				return true;
//				return "탈퇴 되었습니다. 로그아웃 후 메인페이지로 이동합니다.";
			} else {
				return false;
//				return "비밀번호를 다시 입력해주세요.";
			}
		}return false;
//	}return "아이디가 없습니다.";

	}

	// 관리자가 삭제(강제 탈퇴) - 실제로 삭제하는 곳은 서비스 클래스
	@Transactional
	public void deleteByAdmin(String userId, UserRole userRole) {
		if (userRole == UserRole.ADMIN) {
			userRepository.deleteByUserId(userId);
		}
	}

	
	
	
	/////////////////////// 비밀번호 변경
	// 비밀번호 변경할 때 로그인 된 아이디를 기준으로 비밀번호 입력하고 맞으면
	public String updateMemberPassword(UserPasswordChangeDTO dto, String userId) {

		// 아이디 찾고 아이디 존재하지 않을 경우 throw
		Member member = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

		// 입력한 비밀번호가 디비에 저장된 비밀번호와 맞지 않을 경우
		if (!passwordEncoder.matches(dto.getCurrentPwd(), member.getUserPwd())) {
			return "현재 비밀번호가 맞지 않습니다.";
		} else {
			
			// 비밀번호가 맞을 때 새로운 비밀번호 확인
			if (dto.getNewPwd() != dto.getComfirmPwd()) {
				
				// 변경하는 비밀번호 암호화
				dto.setNewPwd(passwordEncoder.encode(dto.getNewPwd()));
				member.setUserPwd(dto.getNewPwd());
				userRepository.save(member);
				return "새로운 비밀번호 확인란을 다시 입력해주세요";
			}else {
				// 비밀번호가 모두 맞을 때 -> 성공
				return "비밀번호 변경에 성공하셨습니다!" + "\n" + "재로그인 해주세요";
			}
		}
	}
	/*public String updateMemberPassword(UserPasswordChangeDTO dto, String userId) {
		
		// 아이디 찾고 아이디 존재하지 않을 경우 throw
		Member member = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));
		
		// 입력한 비밀번호가 디비에 저장된 비밀번호와 맞지 않을 경우
		if (!passwordEncoder.matches(dto.getCurrentPwd(), member.getUserPwd())) {
			return null;
		} else {
			
			// 변경하는 비밀번호 암호화
			dto.setNewPwd(passwordEncoder.encode(dto.getNewPwd()));
			member.setUserPwd(dto.getNewPwd());
			userRepository.save(member);
		}
		
		return member.getUserId();
	}*/

	
	
	
	///////////////////////////////////// 비밀번호 찾기 -> 이메일 전송
	// DB에서의 이메일 유무를 확인하는 서비스
	public boolean checkEmail(String userEmail) {
		// 이메일 o : true | x : false
		log.info("이메일 들어왔니" + userEmail);
		return userRepository.findByUserEmail(userEmail).isPresent();
	}

	// 임시 비밀번호를 암호화, user 객체의 updatePwd 메소드를 호출하여 임시 비밀번호 업데이트
	// 임시 비밀번호 생성
	public String getTmpPwd() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		String pwd = "";

		// 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 조합
		int idx = 0;
		for (int i = 0; i < 10; i++) {
			idx = (int) (charSet.length * Math.random());
			pwd += charSet[idx];
		}
		log.info("임시 비밀번호 생성");

		return pwd;
	}

	// 임시 비밀번호로 업데이트
	public void updatePwd(String tmpPwd, String userEmail) {

		String encryptPassword = passwordEncoder.encode(tmpPwd);
		Member member = userRepository.findByUserEmail(userEmail)
				.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

		// DB의 데이터 값을 변경
		// member.updatePassword(encryptPassword);
		member.setUserPwd(encryptPassword);
		log.info("임시 비밀번호 업데이트");
	}

	public void join(String userId, String userPwd) {
		
		
	}

	/////////////////////// 상태 정보

}
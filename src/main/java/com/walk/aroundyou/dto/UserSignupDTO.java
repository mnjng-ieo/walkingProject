package com.walk.aroundyou.dto;
import java.sql.Timestamp;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domainenum.StateId;
import com.walk.aroundyou.domainenum.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="userPwd")
public class UserSignupDTO {

	////// 유효성 검사와 함께 회원가입
	// dto(데이터 전송) 클래스에서 유효성 검증 어노테이션을 통해 유효성 검사 적용시키기
	// 컨트롤러에서 @Valid 사용하고 Errors통해 유효성 검사 적합 여부를 확인 
	//   (반드시 dto, error순서로 매개변수 작성하기! 
	//     - validation검사할 때 객체 뒤 에러를 받도록 하기 위해)
	// 서비스 클래스에 에러메서드 생성 (유효성 검사 실패 할 경우의 메시지 보내기)
	//    ( Map<key, value> 이용 : 
	//       key : valid_{dto필드명} | value : dto message => format)
	// html에서 타임리프 if문 통해서 유효하지 않을 경우 message 응답 받음
	@NotBlank(message = "아이디를 입력해주세요.")
	@Size(min = 6, max = 15, message = "아이디는 6 ~ 15자 사이로 입력해주세요.")
	private String userId;
	
	// (?=.*[0-9]): 최소한 하나의 숫자(0-9)를 포함
	// (?=.*[a-zA-Z]): 최소한 하나의 영문 알파벳(a-zA-Z)을 포함
	// (?=.*\\W): 최소한 하나의 특수 문자를 포함
	// (?=\\S+$): 공백 문자를 포함하지 않아야 함
	// .{8,16}: 문자열의 길이는 8 ~ 16자 사이
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp ="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String userPwd;
	
	@NotBlank(message = "이름을 입력해주세요.")
	private String userName;
	
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 15, message = "닉네임은 2 ~ 15자 사이로 입력해주세요.")
	private String userNickname;
	
	private String userTelNumber;
	
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 주소를 입력해주세요.")
	private String userEmail;

	private String userDescription;
	private Timestamp userJoinDate;
	private Timestamp userUpdateDate;
	private StateId stateId;
	private UserRole role;
	private boolean socialYn;


	// 멤버메소드
	public Member toEntity() {
		
		return Member.builder()
				.userId(userId)
				.userPwd(userPwd)
				.userName(userName)
				.userNickname(userNickname)
				.userTelNumber(userTelNumber)
				.userEmail(userEmail)
				.userDescription(userDescription)
				.userJoinDate(new Timestamp(System.currentTimeMillis()))
				.userUpdateDate(new Timestamp(System.currentTimeMillis()))
				.role(UserRole.USER)
				.stateId(StateId.NORMAL)
				.socialYn(socialYn)
				.build();
	}

}
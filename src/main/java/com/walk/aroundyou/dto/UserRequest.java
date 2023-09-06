package com.walk.aroundyou.dto;
import java.sql.Timestamp;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domain.role.StateId;
import com.walk.aroundyou.domain.role.UserRole;

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
public class UserRequest {

	/// DTO 클래스, 리포지토리와 상호작용

	// 맴버변수
	private String userId;
	private String userPwd;
	//private String unencodedPwd;
	private String userName;
	private String userNickname;
	private String userTelNumber;
	private String userEmail;
	private String userImg;
	private String userDescription;
	private Timestamp userJoinDate;
	private Timestamp userUpdateDate;
	private StateId stateId;
	private UserRole role;
	private boolean socialYn;


	// 멤버메소드
	public User toEntity() {
		
		return User.builder()
				.userId(userId)
				.userPwd(userPwd)
				//.unencodedPwd(unencodedPwd)
				.userName(userName)
				.userNickname(userNickname)
				.userTelNumber(userTelNumber)
				.userEmail(userEmail)
				.userImg(userImg)
				.userDescription(userDescription)
				.userJoinDate(new Timestamp(System.currentTimeMillis()))
				.userUpdateDate(new Timestamp(System.currentTimeMillis()))
				.role(UserRole.USER)
				.stateId(StateId.NORMAL)
				.socialYn(socialYn)
				.build();

	}

}
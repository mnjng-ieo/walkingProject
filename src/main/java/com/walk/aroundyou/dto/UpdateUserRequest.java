package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domainenum.StateId;
import com.walk.aroundyou.domainenum.UserRole;

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
public class UpdateUserRequest {
	
	/// DTO 클래스, 리포지토리와 상호작용

	
	// 맴버변수
	private String userPwd;
	private String userName;
	private String userNickname;
	private String userTelNumber;
	private String userEmail;
	private Timestamp userJoinDate;
	private Timestamp userUpdateDate;
	private StateId stateId;
	private UserRole userRole;
	private int social;
	
	
	// 멤버메소드
	public User toEntity() {
		
		return null;
	}

	
}

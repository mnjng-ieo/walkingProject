package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.walk.aroundyou.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserpageRequest {

	/// DTO 클래스, 리포지토리와 상호작용

	// 맴버변수
	private String userId;
	private String userName;
	private String userNickname;
	private String userTelNumber;
	private String userEmail;
	private Timestamp userJoinDate;


	// 멤버메소드
	public Member toEntity() {
		
		return Member.builder()
				.userId(userId)
				.userName(userName)
				.userNickname(userNickname)
				.userTelNumber(userTelNumber)
				.userEmail(userEmail)
				.userJoinDate(new Timestamp(System.currentTimeMillis()))
				.build();
	}
	
	public void update(String userId, String userName, String userNickname, String userTelNumber, String userEmail, Timestamp userJoinDate) {
    	this.userId = userId;
    	this.userName = userName;
    	this.userNickname = userNickname;
    	this.userTelNumber = userTelNumber;
    	this.userEmail = userEmail;
    	this.userJoinDate = userJoinDate;
    }

}
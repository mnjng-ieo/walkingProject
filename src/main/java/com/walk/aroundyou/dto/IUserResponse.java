package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface IUserResponse {

	/// 리포지토리와 상호작용
	String getUserId();
	String getUserName();
	String getUserNickname();
	String getUserTelNumber();
	String getUserEmail();
	String getUserDescription();	
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	Timestamp getUserJoinDate();
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	Timestamp getUserUpdateDate();
    String getRole();
    String getStateId();
}
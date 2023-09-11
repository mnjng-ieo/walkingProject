package com.walk.aroundyou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordSendDTO {
	
	private String toaddress; // 받는 이메일 주소
	private String title; // 이메일 제목
	private String message; // 이메일 내용
	private String fromAddress; // 보내는 이메일 주소
}

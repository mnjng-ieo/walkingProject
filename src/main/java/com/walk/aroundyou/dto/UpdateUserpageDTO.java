package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserpageDTO {

	private String userId;
	
	@NotBlank(message = "이름을 입력해주세요.")
	private String userName;
	
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 15, message = "닉네임은 2 ~ 15자 사이로 입력해주세요.")
	private String userNickname;
	
	private String userTelNumber;
	
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 주소를 입력해주세요.")
	private String userEmail;
	
	private Timestamp userJoinDate;
}

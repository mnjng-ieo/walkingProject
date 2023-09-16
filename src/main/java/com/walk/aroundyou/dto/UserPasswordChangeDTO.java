package com.walk.aroundyou.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordChangeDTO {
	
	@NotBlank // 공백 x
	@Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하 대소문자와 숫자를 섞은 값이어야 합니다.")
	private String currentPwd;
	
	@NotBlank
	@Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하 대소문자와 숫자를 섞은 값이어야 합니다.")
	private String newPwd;
	
	@NotBlank
	@Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하 대소문자와 숫자를 섞은 값이어야 합니다.")
	private String comfirmPwd;
	
}

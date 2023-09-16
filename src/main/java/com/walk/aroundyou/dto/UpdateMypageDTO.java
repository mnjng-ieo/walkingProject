package com.walk.aroundyou.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMypageDTO {

	private String userId;
	
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 15, message = "닉네임은 2 ~ 15자 사이로 입력해주세요.")
	private String userNickname;
	
	private String userDescription;
	
}

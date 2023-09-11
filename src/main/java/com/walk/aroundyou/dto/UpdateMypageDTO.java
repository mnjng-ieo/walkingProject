package com.walk.aroundyou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMypageDTO {

	private String userId;
	private String userNickname;
	private String userDescription;
	
}

package com.walk.aroundyou.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
	// comment 수정 시, 필요한 commentContent 필드 
	private String commentContent;
}

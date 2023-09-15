package com.walk.aroundyou.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentLikeRequestDto {
	
	private Long commentId;
	private String userId;
	
	public CommentLikeRequestDto(Long commentId, String userId) {
		this.commentId = commentId;
		this.userId = userId;
	}

}

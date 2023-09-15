package com.walk.aroundyou.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLikeRequestDTO {

	private Long boardId;
	private String userId;
	
	public BoardLikeRequestDTO(
			Long boardId, String userId) {
		this.boardId = boardId;
		this.userId = userId;
	}
}

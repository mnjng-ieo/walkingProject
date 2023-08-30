package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.domain.User;

import lombok.Getter;

@Getter
public class BoardLikeResponse {

	private long boardLikeId;
	private Board boardId;
	private User userId;
	
	public BoardLikeResponse(BoardLike boardLike) {
		
		this.boardLikeId = boardLike.getBoardLikeId();
		this.boardId = boardLike.getBoardId();
		this.userId = boardLike.getUserId();
	}
	
	
}

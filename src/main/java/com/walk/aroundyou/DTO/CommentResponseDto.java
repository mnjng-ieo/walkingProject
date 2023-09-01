package com.walk.aroundyou.DTO;

import com.walk.aroundyou.domainenum.CommentType;

// * 응답을 위한 DTO
public interface CommentResponseDto {

	//User getUserId();
	String getUserNickname();
	String getCommentContent();
	CommentType getCommentType();
	//Integer getCommentLikeCnt();
	
}
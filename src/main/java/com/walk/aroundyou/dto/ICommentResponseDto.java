package com.walk.aroundyou.dto;

import com.walk.aroundyou.domainenum.CommentType;

// * 응답을 위한 DTO
public interface ICommentResponseDto {

	//User getUserId();
	String getUserNickname();
	String getCommentContent();
	CommentType getCommentType();
	//Integer getCommentLikeCnt();
	
}
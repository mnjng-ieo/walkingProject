package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walk.aroundyou.domainenum.CommentType;

// * 응답을 위한 DTO
public interface ICommentResponseDto {
	
	String getCourseId();
	String getBoardId();
	String getCommentId();
	String getUserId();
	String getUserNickname();
	String getCommentContent();
	long getCommentLikeCnt();
	// @JsonFormat 어노테이션의 pattern 속성을 이용해 원하는 날짜 형식으로 변환 가능 
	// 단, 뷰(html)에서 타임리프를 이용해 다시 변환할 날짜 형식을 지정해주어야 변경됨 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
	Timestamp getCommentUpdatedDate();
	CommentType getCommentType();
}


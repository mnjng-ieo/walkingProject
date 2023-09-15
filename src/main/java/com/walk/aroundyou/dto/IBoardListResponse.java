package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

// 좋아요 수와 댓글 수를 같이 전송하는 DTO클래스
public interface IBoardListResponse {
	
	Long getBoardId();
	String getBoardType();
//	private String boardCategory;
	String getBoardTitle();
	String getBoardContent();
	String getUserNickname();
	String getUserId();
	Integer getBoardViewCount();
	// Json으로 데이터를 보낼떄 날짜형식을 지정해주는 어노테이션
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	Timestamp getBoardCreatedDate();
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	Timestamp getBoardUpdatedDate();
//	private boolean boardSecret;
	Integer getCommentCnt();
	Integer getLikeCnt();
	
	
	
}

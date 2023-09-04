package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


// @Data 
// DTO이지만 응답을 하기 위한 DTO 이므로, @Setter 속성 함수는 쓰이지 않음 (@Setter를 내장하고 있는 @Data 어노테이션은 주석 처리)
@Builder
@ToString
public class BoardLikeDTO {

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board boardId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User userId;
	
	
	
	// 
	public BoardLikeDTO() {
		
	}
	
}
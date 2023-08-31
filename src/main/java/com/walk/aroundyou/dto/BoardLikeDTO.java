package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;



@Data
@Builder
@ToString
public class BoardLikeDTO {

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board boardId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User userId;
	
}

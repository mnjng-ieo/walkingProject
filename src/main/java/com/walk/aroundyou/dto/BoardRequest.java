package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.domainenum.StateId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
	private Long boardId;
	private String userId;
	private String userNickname;
	private BoardType boardType;
	private String boardTitle;
	private String boardContent;
	
	public Board toEntity() {
		return Board.builder()
				.boardId(boardId)
				.userId(
						User.builder()
						.userId(userId)
						.build())
				.userNickname(userNickname)
				.boardType(boardType)
				.boardTitle(boardTitle)
				.boardContent(boardContent)
				.boardCreatedDate(new Timestamp(System.currentTimeMillis()))
				.boardUpdatedDate(new Timestamp(System.currentTimeMillis()))
				.stateId(StateId.NORMAL)
				.build();
	}
}

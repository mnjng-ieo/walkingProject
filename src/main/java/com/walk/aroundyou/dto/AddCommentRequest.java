package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domainenum.CommentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// * 컨트롤러에서 요청 본문을 받을 DTO
@NoArgsConstructor
@AllArgsConstructor
@Data
///9/12 Getter->Data

// * 포스트맨으로 확인 시, 'JSON parse error: Cannot construct instance of~' 오류 발생에 대한 해결법
//   : 아래 어노테이션 추가
@JsonAutoDetect 
public class AddCommentRequest {
	
	private Member userId;
	private String userNickname;
	private Long commentId;
	private String commentContent;
	private CommentType commentType;
	private Timestamp commentCreatedDate;
	private Timestamp commentUpdatedDate;
	private Board boardId;
	private Course courseId;
	
	
	// 1) commentType = 'BOARD'인 경우 ( DTO -> Entity )
	public Comment toBoardEntity() {	
		return Comment.builder()
				.userId(userId)
				.userNickname(userNickname)
				.commentContent(commentContent)
				.commentType(CommentType.BOARD)
				.commentCreatedDate(commentCreatedDate)
				.commentUpdatedDate(new Timestamp(System.currentTimeMillis()))
				.boardId(boardId)
				.build();
	}
	
	
	// 2) commentType = 'COURSE'인 경우 ( DTO -> Entity )
	public Comment toCourseEntity() {	
		return Comment.builder()
				.userId(userId)
				.userNickname(userNickname)
				.commentContent(commentContent)
				.commentType(CommentType.COURSE)
				.commentCreatedDate(commentCreatedDate)
				.commentUpdatedDate(new Timestamp(System.currentTimeMillis()))
				.courseId(courseId)
				.build();
	}
	
}


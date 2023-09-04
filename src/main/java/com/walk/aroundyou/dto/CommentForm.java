package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domainenum.CommentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentForm {
	
	private User userId;
	private String userNickname;
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
				.commentCreatedDate(new Timestamp(System.currentTimeMillis()))
				.commentUpdatedDate(new Timestamp(System.currentTimeMillis()))
				.commentType(CommentType.BOARD)
				.boardId(boardId)
				.build();
	}
	
	
	// 2) commentType = 'COURSE'인 경우 ( DTO -> Entity )
	public Comment toCourseEntity() {	
		return Comment.builder()
				.userId(userId)
				.userNickname(userNickname)
				.commentContent(commentContent)
				.commentUpdatedDate(commentCreatedDate)
				.commentType(CommentType.COURSE)
				.courseId(courseId)
				.build();
	}

	
}

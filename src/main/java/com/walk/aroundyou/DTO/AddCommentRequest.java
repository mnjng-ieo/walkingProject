package com.walk.aroundyou.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domainenum.CommentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// * 컨트롤러에서 요청 본문을 받을 DTO
@NoArgsConstructor
@AllArgsConstructor
@Getter

// * 포스트맨으로 확인 시, 'JSON parse error: Cannot construct instance of~' 오류 발생에 대한 해결법
//   : 아래 어노테이션 추가
@JsonAutoDetect 
public class AddCommentRequest {
	
	private User userId;
	private String commentContent;
	private String userNickname;
	private CommentType commentType;
	private Board boardId;
	private Course courseId;
	
	
	// 1) commentType = 'BOARD'인 경우 ( DTO -> Entity )
	public Comment toBoardEntity() {	
		return Comment.builder()
				.userId(userId)
				.commentContent(commentContent)
				.userNickname(userNickname)
				.commentType(CommentType.BOARD)
				.boardId(boardId)
				.build();
	}
	
	
	// 2) commentType = 'COURSE'인 경우 ( DTO -> Entity )
	public Comment toCourseEntity() {	
		return Comment.builder()
				.userId(userId)
				.commentContent(commentContent)
				.userNickname(userNickname)
				.commentType(CommentType.COURSE)
				.courseId(courseId)
				.build();
	}
	
}


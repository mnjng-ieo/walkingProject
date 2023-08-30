package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardCourse;
import com.walk.aroundyou.domain.Course;

import lombok.Getter;

@Getter
public class BoardCourseResponse {

	private long boardCourseId;
	private Board boardId;
	private Course courseId;
	
	public BoardCourseResponse(BoardCourse boardCourse) {
		this.boardCourseId = boardCourse.getBoardCourseId();
		this.boardId = boardCourse.getBoardId();
		this.courseId = boardCourse.getCourseId();
	}
	
}

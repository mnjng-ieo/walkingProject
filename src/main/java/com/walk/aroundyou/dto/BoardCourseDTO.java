package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class BoardCourseDTO {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board boardId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course courseId;
	
}

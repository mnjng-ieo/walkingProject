package com.walk.aroundyou.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="board_course")
public class BoardCourses {

	@Id
	@Column(name="board_course_id", columnDefinition="bigint", nullable=false)
	private long BoardCourseId;
	
	
	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Boards BoardId;
	
	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	private Course CourseId;
	
}


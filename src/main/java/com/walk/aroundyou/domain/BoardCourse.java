package com.walk.aroundyou.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="board_course")
public class BoardCourse {

	@Id
	@Column(name="board_course_id", columnDefinition="bigint", nullable=false)
	private Long boardCourseId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board boardId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName="course_id", nullable = false)
	private Course courseId;
}

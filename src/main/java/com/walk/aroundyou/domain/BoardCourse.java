package com.walk.aroundyou.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long boardCourseId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "board_id", nullable = false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Board boardId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id", nullable = false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Course courseId;
	
}


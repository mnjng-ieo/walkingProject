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
public class BoardCourse {

	@Id
	@Column(name="bcourse_id", columnDefinition="bigint", nullable=false)
	private long bcourseId;
	
	
	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board boardId;
	
	@ManyToOne
	@JoinColumn(name = "esntl_id", nullable = false)
	private Course esntlId;
	
}


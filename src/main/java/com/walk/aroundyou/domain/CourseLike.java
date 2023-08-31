package com.walk.aroundyou.domain;

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

@Builder
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="course_like")
public class CourseLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="course_like_id",columnDefinition="bigint", nullable=false)
	private long courseLikeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
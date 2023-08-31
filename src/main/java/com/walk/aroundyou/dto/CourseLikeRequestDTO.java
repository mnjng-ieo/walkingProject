package com.walk.aroundyou.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseLikeRequestDTO {

	private Long courseId;
	private Long userId;
	
	public CourseLikeRequestDTO(
			Long courseId, Long userId) {
		this.courseId = courseId;
		this.userId = userId;
	}
}

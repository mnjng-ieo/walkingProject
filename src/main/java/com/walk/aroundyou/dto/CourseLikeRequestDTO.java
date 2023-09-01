package com.walk.aroundyou.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseLikeRequestDTO {

	private Long courseId;
	private String userId;
	
	public CourseLikeRequestDTO(
			Long courseId, String userId) {
		this.courseId = courseId;
		this.userId = userId;
	}
}

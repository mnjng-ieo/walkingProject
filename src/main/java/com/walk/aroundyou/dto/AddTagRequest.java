package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddTagRequest {

	private String tagContent;
	
	public Tag toEntity() {
		return Tag.builder()
			.tagContent(tagContent)
			.build();
	}
}

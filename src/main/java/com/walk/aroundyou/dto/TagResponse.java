package com.walk.aroundyou.dto;

import com.walk.aroundyou.domain.Tag;

import lombok.Getter;

@Getter
public class TagResponse {

	private final String tagContent;

	public TagResponse(Tag tag) {
		this.tagContent = tag.getTagContent();
	}
}

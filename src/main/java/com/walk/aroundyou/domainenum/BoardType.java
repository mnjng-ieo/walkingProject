package com.walk.aroundyou.domainenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType {
	FREE("자유"),
	COMMUNIY("모임"),
	REVIEW("후기");
	
	private final String kor;
}

package com.walk.aroundyou.domain.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum StateId {
	NORMAL("이상없음"),
	NEED_TO_CHECK("문제검토"),
	NEED_TO_DELETE("삭제예정"),
	SELF_WITHDRAW("회원탈퇴"),
	FORCE_WITHDRAW("회원강퇴"),
	ETC("기타");
	
	private final String kor;
	
}

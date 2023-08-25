package com.walk.aroundyou.domainenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateId {
	NORMAL("이상없음"),
	NEED_TO_DELETE("삭제예정"),
	NEED_TO_CHECK("문제검토"),
	SELF_WITHDRAW("회원탈퇴"),
	FORCE_WITHDRAW("회원강퇴"),
	ETC("기타");
	
	private final String kor;
	
}
